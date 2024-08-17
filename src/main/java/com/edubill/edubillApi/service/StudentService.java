package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.dto.group.DeletedGroupInfoDto;
import com.edubill.edubillApi.dto.group.GroupInfoRequestDto;
import com.edubill.edubillApi.dto.group.GroupInfoResponseDto;
import com.edubill.edubillApi.dto.group.GroupInfoInAddStudentResponseDto;

import com.edubill.edubillApi.dto.student.*;
import com.edubill.edubillApi.error.exception.GroupNotFoundException;
import com.edubill.edubillApi.error.exception.StudentNotFoundException;
import com.edubill.edubillApi.repository.StudentGroupRepository;
import com.edubill.edubillApi.repository.StudentPaymentHistoryRepository;
import com.edubill.edubillApi.repository.group.GroupRepository;
import com.edubill.edubillApi.repository.ClassTimeRepository;

import com.edubill.edubillApi.repository.payment.PaymentKeyRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;

import com.edubill.edubillApi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final ClassTimeRepository classTimeRepository;
    private final PaymentKeyRepository paymentKeyRepository;
    private final StudentPaymentHistoryRepository studentPaymentHistoryRepository;

    @Transactional
    public StudentInfoResponseDto addStudentInfo(StudentInfoRequestDto studentInfoRequestDto) {

        Student savedStudent = studentRepository.save(Student.from(studentInfoRequestDto));

        List<StudentGroup> studentGroups = new ArrayList<>();
        for (Long groupId : studentInfoRequestDto.getGroupIds()) {
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new GroupNotFoundException("Group not found with id " + groupId));

            group.addStudentCount();

            StudentGroup studentGroup = StudentGroup.builder()
                    .student(savedStudent)
                    .group(group)
                    .build();
            studentGroup.setStudent(savedStudent); // Student 설정
            studentGroup.setGroup(group);   // Group 설정
            studentGroups.add(studentGroup);
        }
        studentGroupRepository.saveAll(studentGroups);

        return StudentInfoResponseDto.createStudentInfoResponse(savedStudent, studentInfoRequestDto.getGroupIds());
    }

    @Transactional
    public GroupInfoResponseDto addGroupInfo(GroupInfoRequestDto groupInfoRequestDto) {
        String userId = SecurityUtils.getCurrentUserId();
        List<GroupInfoRequestDto.ClassTimeRequestDto> classTimeRequestDtos = groupInfoRequestDto.getClassTimeRequestDtos();

        Group savedGroup = groupRepository.save(Group.from(groupInfoRequestDto, userId));

        // DTO를 도메인 객체로 변환
        List<ClassTime> classTimes = classTimeRequestDtos.stream()
                .map(dto -> {
                    // ClassTime 객체를 생성
                    ClassTime classTime = ClassTime.builder()
                            .dayOfWeek(dto.getDayOfWeek())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .build();
                    // 연관관계 설정
                    classTime.setGroup(savedGroup);
                    return classTime;
                })
                .collect(Collectors.toList());
        classTimeRepository.saveAll(classTimes);

        return GroupInfoResponseDto.createGroupInfoResponse(savedGroup, classTimes);
    }

    @Transactional(readOnly = true)
    public Page<GroupInfoInAddStudentResponseDto> findAllGroupsByUserId(Pageable pageable) {
        Page<Group> groups = groupRepository.getGroupsByUserIdWithPaging(SecurityUtils.getCurrentUserId(),pageable);

        return groups.map(group -> {
            List<GroupInfoInAddStudentResponseDto.ClassTimeResponseDto> classTimeResponseDtos = group.getClassTimes().stream()
                    .map(GroupInfoInAddStudentResponseDto.ClassTimeResponseDto::createClassTimeResponse)
                    .collect(Collectors.toList());

            return GroupInfoInAddStudentResponseDto.builder()
                    .groupId(group.getId())
                    .groupName(group.getGroupName())
                    .classTimeResponseDtos(classTimeResponseDtos)
                    .build();
        });
    }

    @Transactional
    public DeletedStudentInfoDto deleteStudentInfo(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        // 자식 엔티티 목록 가져오기
        List<StudentGroup> studentGroups = student.getStudentGroups();
        List<PaymentKey> paymentKeys = student.getPaymentKeyList();
        List<StudentPaymentHistory> studentPaymentHistories = student.getStudentPaymentHistories();

        // ID 목록 추출
        List<Long> deletedStudentGroupIds = extractIds(studentGroups, StudentGroup::getStudentGroupId);
        List<Long> deletedPaymentKeyIds = extractIds(paymentKeys, PaymentKey::getId);
        List<Long> deletedStudentPaymentHistoryIds = extractIds(studentPaymentHistories, StudentPaymentHistory::getStudentPaymentHistoryId);

        // 학생이 속한 그룹에서 학생 수 감소
        for (StudentGroup studentGroup : studentGroups) {
            Group group = groupRepository.findById(studentGroup.getGroup().getId())
                    .orElseThrow(() -> new GroupNotFoundException("Group not found with id " + studentGroup.getGroup().getId()));
            group.removeStudentCount();
            groupRepository.save(group);
        }

        // 자식 엔티티 삭제
        studentGroupRepository.deleteAll(studentGroups);
        paymentKeyRepository.deleteAll(paymentKeys);
        studentPaymentHistoryRepository.deleteAll(studentPaymentHistories);

        // 부모 엔티티 삭제
        studentRepository.deleteById(studentId);

        return DeletedStudentInfoDto.builder()
                .deletedStudentId(studentId)
                .deletedStudentGroupIds(deletedStudentGroupIds)
                .deletedPaymentKeyIds(deletedPaymentKeyIds)
                .deletedStudentPaymentHistoryIds(deletedStudentPaymentHistoryIds)
                .build();
    }

    @Transactional
    public DeletedGroupInfoDto deleteGroupInfo(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("group not found"));

        List<ClassTime> classTimes = group.getClassTimes();
        List<StudentGroup> studentGroups = group.getStudentGroups();

        List<Long> deletedClassTimeIds = extractIds(classTimes, ClassTime::getClassTimeId);
        List<Long> deletedStudentGroupIds = extractIds(studentGroups, StudentGroup::getStudentGroupId);

        classTimeRepository.deleteAll(classTimes);
        studentGroupRepository.deleteAll(studentGroups);
        groupRepository.deleteById(groupId);

        return DeletedGroupInfoDto.builder()
                .deletedGroupId(groupId)
                .deletedClassTimeIds(deletedClassTimeIds)
                .deletedStudentGroupIds(deletedStudentGroupIds)
                .build();
    }

    //테스트 용
    @Transactional
    public void addStudentAndGroupInfoTest(StudentInfoTestRequestDto studentInfoTestRequestDto, final String userId) {

        List<Group> groups = groupRepository.getGroupsByUserId(userId);
        Group group = null;
        if (!groups.isEmpty()) {
            group = groups.get(0);
        } else {
            group = Group.builder()
                    .groupName("중등 B반")
                    .managerId(userId)
                    .tuition(300000)
                    .build();
        }
        //학생수 증가
        group.addStudentCount();
        Student student = Student.builder()
                .studentName(studentInfoTestRequestDto.getStudentName())
                .studentPhoneNumber(studentInfoTestRequestDto.getStudentPhoneNumber())
                .parentName(studentInfoTestRequestDto.getParentName())
                .parentPhoneNumber(studentInfoTestRequestDto.getParentPhoneNumber())
                .build();
        studentRepository.save(student);

        StudentGroup studentGroup = StudentGroup.builder()
                .build(); // 빈 상태로 생성
        studentGroup.setStudent(student); // Student 설정
        studentGroup.setGroup(group);   // Group 설정

        studentGroupRepository.save(studentGroup);
        groupRepository.save(group);
    }

    private <T> List<Long> extractIds(List<T> entities, Function<T, Long> idExtractor) {
        return entities.stream()
                .map(idExtractor)
                .collect(Collectors.toList());
    }
}
