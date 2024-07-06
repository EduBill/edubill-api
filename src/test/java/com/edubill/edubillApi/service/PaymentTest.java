package com.edubill.edubillApi.service;

import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.domain.*;
import com.edubill.edubillApi.dto.payment.UnpaidStudentsResponseDto;
import com.edubill.edubillApi.repository.StudentPaymentHistoryRepository;
import com.edubill.edubillApi.repository.payment.PaymentHistoryRepository;
import com.edubill.edubillApi.repository.student.StudentRepository;
import com.edubill.edubillApi.repository.studentgroup.StudentGroupRepository;
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
import java.util.stream.Collectors;

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
    StudentGroupRepository studentGroupRepository;
    @Autowired
    StudentPaymentHistoryRepository studentPaymentHistoryRepository;
    @Autowired
    PaymentHistoryRepository paymentHistoryRepository;


    @Test
    @DisplayName("미납학생 조회")
    @Transactional
    void getUnpaidStudentsTest() {

        //given
        User user = User.builder()
                .userId("1")
                .userName("manager1")
                .phoneNumber("01011111111")
                .build();
        userRepository.save(user);

        StudentGroup studentGroup1 = StudentGroup.builder()
                .groupName("group1")
                .managerId(user.getUserId())
                .build();
        studentGroupRepository.save(studentGroup1);

        StudentGroup studentGroup2 = StudentGroup.builder()
                .groupName("group2")
                .managerId(user.getUserId())
                .build();
        studentGroupRepository.save(studentGroup2);

        Student student1 = Student.builder()
                .studentName("s1")
                .studentGroup(studentGroup1)
                .studentPhoneNumber("01012341234")
                .build();
        studentRepository.save(student1);

        Student student2 = Student.builder()
                .studentName("s2")
                .studentGroup(studentGroup1)
                .studentPhoneNumber("01056785678")
                .build();
        studentRepository.save(student2);

        Student student3 = Student.builder()
                .studentName("s3")
                .studentGroup(studentGroup1)
                .studentPhoneNumber("01009870987")
                .build();
        studentRepository.save(student3);

        PaymentHistory paymentHistory1 = PaymentHistory.builder()
                .paymentStatus(PaymentStatus.PAID)
                .managerId(user.getUserId())
                .depositorName(student1.getStudentName())
                .build();
        paymentHistoryRepository.save(paymentHistory1);

        PaymentHistory paymentHistory2 = PaymentHistory.builder()
                .paymentStatus(PaymentStatus.PAID)
                .managerId(user.getUserId())
                .depositorName(student2.getStudentName())
                .build();
        paymentHistoryRepository.save(paymentHistory2);

        StudentPaymentHistory studentPaymentHistory1 = StudentPaymentHistory.builder()
                .student(student1)
                .paymentHistory(paymentHistory1)
                .yearMonth(YearMonth.of(2024,6).toString())
                .build();
        studentPaymentHistoryRepository.save(studentPaymentHistory1);

        StudentPaymentHistory studentPaymentHistory2 = StudentPaymentHistory.builder()
                .student(student2)
                .paymentHistory(paymentHistory2)
                .yearMonth(YearMonth.of(2024,6).toString())
                .build();
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
        User user = User.builder()
                .userId("1")
                .userName("manager1")
                .phoneNumber("01011111111")
                .build();
        userRepository.save(user);

        StudentGroup studentGroup1 = StudentGroup.builder()
                .groupName("group1")
                .managerId(user.getUserId())
                .build();
        studentGroupRepository.save(studentGroup1);

        StudentGroup studentGroup2 = StudentGroup.builder()
                .groupName("group2")
                .managerId(user.getUserId())
                .build();
        studentGroupRepository.save(studentGroup2);

        Student student1 = Student.builder()
                .studentName("s1")
                .studentGroup(studentGroup1)
                .studentPhoneNumber("01012341234")
                .build();
        studentRepository.save(student1);

        Student student2 = Student.builder()
                .studentName("s2")
                .studentGroup(studentGroup1)
                .studentPhoneNumber("01056785678")
                .build();
        studentRepository.save(student2);

        Student student3 = Student.builder()
                .studentName("s3")
                .studentGroup(studentGroup1)
                .studentPhoneNumber("01009870987")
                .build();
        studentRepository.save(student3);

        PaymentHistory paymentHistory1 = PaymentHistory.builder()
                .paymentStatus(PaymentStatus.PAID)
                .managerId(user.getUserId())
                .depositorName(student1.getStudentName())
                .build();
        paymentHistoryRepository.save(paymentHistory1);

        PaymentHistory paymentHistory2 = PaymentHistory.builder()
                .paymentStatus(PaymentStatus.PAID)
                .managerId(user.getUserId())
                .depositorName(student2.getStudentName())
                .build();
        paymentHistoryRepository.save(paymentHistory2);

        StudentPaymentHistory studentPaymentHistory1 = StudentPaymentHistory.builder()
                .student(student1)
                .paymentHistory(paymentHistory1)
                .yearMonth(YearMonth.of(2024,6).toString())
                .build();
        studentPaymentHistoryRepository.save(studentPaymentHistory1);

        StudentPaymentHistory studentPaymentHistory2 = StudentPaymentHistory.builder()
                .student(student2)
                .paymentHistory(paymentHistory2)
                .yearMonth(YearMonth.of(2024,6).toString())
                .build();
        studentPaymentHistoryRepository.save(studentPaymentHistory2);

        //when
        List<Student> paidStudentsResponseDtos = studentRepository.findPaidStudentsByYearMonthAndManagerId(user.getUserId(), YearMonth.of(2024,6));

        //then
//        assertThat(student1.getId()).isEqualTo(studentPaymentHistory1.getStudent().getId());
        assertThat(paidStudentsResponseDtos.size()).isEqualTo(2);

    }

}
