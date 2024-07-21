package com.edubill.edubillApi.service;

import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.domain.enums.PaymentStatus;
import com.edubill.edubillApi.dto.payment.UnpaidStudentsResponseDto;
import com.edubill.edubillApi.repository.StudentPaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentHistoryRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
import com.edubill.edubillApi.repository.group.GroupRepository;
import com.edubill.edubillApi.repository.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
import java.util.List;

@SpringBootTest
@Slf4j
@TestcontainerConfig
public class PaymentTest {

    @Autowired
    PaymentService paymentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    StudentPaymentHistoryRepository studentPaymentHistoryRepository;
    @Autowired
    PaymentHistoryRepository paymentHistoryRepository;


    private User createUser(String userId, String userName, String phoneNumber) {
        return User.builder()
                .userId(userId)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .build();
    }

    private Student createStudent(String studentName, Group group, String phoneNumber) {
        return Student.builder()
                .studentName(studentName)
                //.group(group)
                .studentPhoneNumber(phoneNumber)
                .build();
    }

    private Group createStudentGroup(String groupName, String managerId) {
        return Group.builder()
                .groupName(groupName)
                .managerId(managerId)
                .build();
    }

    private PaymentHistory createPaymentHistory(PaymentStatus paymentStatus, String managerId, String depositorName) {
        return PaymentHistory.builder()
                .paymentStatus(paymentStatus)
                .managerId(managerId)
                .depositorName(depositorName)
                .build();
    }

    private StudentPaymentHistory createStudentPaymentHistory(Student student, PaymentHistory paymentHistory, String yearMonth) {
        return StudentPaymentHistory.builder()
                .student(student)
                .paymentHistory(paymentHistory)
                .yearMonth(yearMonth)
                .build();
    }

    @Test
    @DisplayName("미납학생 조회")
    @Transactional
    void getUnpaidStudentsTest() {

        //given
        User user = createUser("1", "manager1", "01022222222");
        userRepository.save(user);

        Group group1 = createStudentGroup("group1", user.getUserId());
        groupRepository.save(group1);

        Student student1 = createStudent("s1", group1, "01012341234");
        studentRepository.save(student1);

        Student student2 = createStudent("s2", group1, "01056785678");
        studentRepository.save(student2);

        Student student3 = createStudent("s3", group1, "01009870987");
        studentRepository.save(student3);

        PaymentHistory paymentHistory1 = createPaymentHistory(PaymentStatus.PAID, user.getUserId(), student1.getStudentName());
        paymentHistoryRepository.save(paymentHistory1);

        PaymentHistory paymentHistory2 = createPaymentHistory(PaymentStatus.PAID, user.getUserId(), student2.getStudentName());
        paymentHistoryRepository.save(paymentHistory2);

        StudentPaymentHistory studentPaymentHistory1 = createStudentPaymentHistory(student1, paymentHistory1,YearMonth.of(2024,6).toString());
        studentPaymentHistoryRepository.save(studentPaymentHistory1);

        StudentPaymentHistory studentPaymentHistory2 = createStudentPaymentHistory(student2, paymentHistory2,YearMonth.of(2024,6).toString());
        studentPaymentHistoryRepository.save(studentPaymentHistory2);

        //when
        List<UnpaidStudentsResponseDto> unpaidStudentsList = paymentService.getUnpaidStudentsList(user.getUserId(), YearMonth.of(2024, 6));

        //then
        assertThat(unpaidStudentsList.get(0).getStudentName()).isEqualTo("s3 0987");
        assertThat(unpaidStudentsList.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("납입 내역 조회")
    @Transactional
    void paidStudents(){
        //given
        User user = createUser("1", "manager1", "01022222222");
        userRepository.save(user);

        Group group1 = createStudentGroup("group1", user.getUserId());
        groupRepository.save(group1);

        Student student1 = createStudent("s1", group1, "01012341234");
        studentRepository.save(student1);

        Student student2 = createStudent("s2", group1, "01056785678");
        studentRepository.save(student2);

        Student student3 = createStudent("s3", group1, "01009870987");
        studentRepository.save(student3);

        PaymentHistory paymentHistory1 = createPaymentHistory(PaymentStatus.PAID, user.getUserId(), student1.getStudentName());
        paymentHistoryRepository.save(paymentHistory1);

        PaymentHistory paymentHistory2 = createPaymentHistory(PaymentStatus.PAID, user.getUserId(), student2.getStudentName());
        paymentHistoryRepository.save(paymentHistory2);

        StudentPaymentHistory studentPaymentHistory1 = createStudentPaymentHistory(student1, paymentHistory1,YearMonth.of(2024,6).toString());
        studentPaymentHistoryRepository.save(studentPaymentHistory1);

        StudentPaymentHistory studentPaymentHistory2 = createStudentPaymentHistory(student2, paymentHistory2,YearMonth.of(2024,6).toString());
        studentPaymentHistoryRepository.save(studentPaymentHistory2);

        //when
        List<Student> paidStudentsResponseDtos = studentRepository.findPaidStudentsByYearMonthAndManagerId(user.getUserId(), YearMonth.of(2024,6));

        //then
//        assertThat(student1.getId()).isEqualTo(studentPaymentHistory1.getStudent().getId());
        assertThat(paidStudentsResponseDtos.size()).isEqualTo(2);

    }

}
