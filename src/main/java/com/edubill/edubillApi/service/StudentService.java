package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.dto.group.*;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final ClassTimeRepository classTimeRepository;
    private final PaymentKeyRepository paymentKeyRepository;
    private final StudentPaymentHistoryRepository studentPaymentHistoryRepository;

    private Page<StudentAndGroupResponseDto> studentMapToStudentAndGroupResponseDtowWithPaging(Page<Student> students){
        return students.map(student -> {
            // 학생이 속한 그룹들의 className을 가져오기
            List<String> classNames = student.getStudentGroups().stream()
                    .map(studentGroup -> studentGroup.getGroup().getGroupName())
                    .collect(Collectors.toList());

            return StudentAndGroupResponseDto.builder()
                    .studentId(student.getId())
                    .studentName(student.getStudentName())
                    .parentName(student.getParentName())
                    .classNames(classNames)
                    .build();
        });
    }

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

    public Page<StudentAndGroupResponseDto> findAllStudentsByUserId(Pageable pageable, String isUnpaid) {

        if (isUnpaid.equals("false")) { // 모든 학생 조회
            Page<Student> students = studentRepository.getStudentsByUserIdWithPaging(SecurityUtils.getCurrentUserId(),pageable);

            return studentMapToStudentAndGroupResponseDtowWithPaging(students);
        }
        else{ // 미납입자만 조회
            YearMonth yearMonth = YearMonth.now();
            Page<Student> students = studentRepository.findUnpaidStudentsByYearMonthAndManagerId(SecurityUtils.getCurrentUserId(), yearMonth, pageable);

            return studentMapToStudentAndGroupResponseDtowWithPaging(students);
        }

    }

    public Page<StudentAndGroupResponseDto> findStudentsByUserIdAndGroupIdOrNameOrPhoneNum(Pageable pageable, String isUnpaid, Long groupId, String nameOrPhoneNum) {

        String currentId = SecurityUtils.getCurrentUserId();
        if (isUnpaid.equals("false")){ // 납입 여부 구분 안함
            Page<Student> students = studentRepository.getStudentsByUserIdAndGroupIdOrStudentNameOrPhoneNumWithPaging(currentId, pageable, groupId, nameOrPhoneNum);
            return studentMapToStudentAndGroupResponseDtowWithPaging(students);
        }
        else { // 미납입자 조회
            Page<Student> students = studentRepository.getUnpaidStudentsByUserIdAndGroupIdOrStudentNameOrPhoneNumWithPaging(currentId, pageable, groupId, nameOrPhoneNum);
            return studentMapToStudentAndGroupResponseDtowWithPaging(students);

        }
    }

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

    public GroupInfoResponseDto findGroupDetailById(long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("반이 존재하지 않습니다."));
        List<ClassTime> classTimes = classTimeRepository.findByGroupId(groupId);

        return GroupInfoResponseDto.createGroupInfoResponse(group, classTimes);
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

    public StudentInfoDetailResponse getStudentInfo(Long studentId) {
        final Student findStudent = studentRepository.findById(studentId).orElseThrow(
                () -> new StudentNotFoundException("Student not found with id " + studentId)
        );

        return StudentInfoDetailResponse.of(findStudent);
    }

    private <T> List<Long> extractIds(List<T> entities, Function<T, Long> idExtractor) {
        return entities.stream()
                .map(idExtractor)
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupInfoResponseDto updateGroupInfo(Long groupId, GroupInfoRequestDto groupInfoRequestDto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(()-> new GroupNotFoundException("Group not found with id " + groupId));
        List<GroupInfoRequestDto.ClassTimeRequestDto> classTimeRequestDtos = groupInfoRequestDto.getClassTimeRequestDtos();

        // 그룹에 대한 기본적인 정보 변경
        Group updatedGroup = group.updateGroup(groupInfoRequestDto);

        // 수업 시간 업데이트
        List<ClassTime> oldClassTime = group.getClassTimes();

        List<ClassTime> newClassTimes = classTimeRequestDtos.stream()
                .map(dto -> {
                    // ClassTime 객체를 생성
                    ClassTime classTime = ClassTime.builder()
                            .dayOfWeek(dto.getDayOfWeek())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .build();
                    return classTime;
                })
                .collect(Collectors.toList());

        // 기존 수업시간과 새로운 수업시간 비교하여 삭제해야할 classTime 객체 찾기
        List<ClassTime> deletedClassTime = oldClassTime.stream().filter(o -> newClassTimes.stream().noneMatch(n ->{
            return o.getDayOfWeek().equals(n.getDayOfWeek()) && o.getStartTime().equals(n.getStartTime()) && o.getEndTime().equals(n.getEndTime());
        })).collect(Collectors.toList());

        deletedClassTime.stream().forEach(classTime -> {
            updatedGroup.getClassTimes().remove(classTime);
            classTimeRepository.deleteByClassTimeId(classTime.getClassTimeId());
        });

        // 새로운 classTime 객체 저장하기
        List<ClassTime> createdClassTime = newClassTimes.stream().filter(n -> oldClassTime.stream().noneMatch(o ->{
            return o.getDayOfWeek().equals(n.getDayOfWeek()) && o.getStartTime().equals(n.getStartTime()) && o.getEndTime().equals(n.getEndTime());
        })).collect(Collectors.toList());

        createdClassTime.stream().forEach(classTime -> classTime.setGroup(updatedGroup));
        classTimeRepository.saveAll(createdClassTime);


        return GroupInfoResponseDto.createGroupInfoResponse(updatedGroup, updatedGroup.getClassTimes());
    }
}
