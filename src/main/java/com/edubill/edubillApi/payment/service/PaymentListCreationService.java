package com.edubill.edubillApi.payment.service;

import com.edubill.edubillApi.payment.domain.PaymentHistory;

import com.edubill.edubillApi.domain.StudentGroup;

import com.edubill.edubillApi.user.repository.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentListCreationService {


    private final StudentGroupRepository studentGroupRepository;

    public void createPaymentInfoList(LocalDate depositDate, String depositorName, String bankName, int depositAmount, String memo, List<PaymentHistory> paymentHistories, String userId) {


        List<StudentGroup> studentGroups = studentGroupRepository.getUserGroupsByUserId(userId);

        if (studentGroups != null && !studentGroups.isEmpty()) {
            //TODO: StudentGroupId에 대한 정보를 받고 해당하는 Id에 따라 paymentInfo를 저장하도록 수정
            StudentGroup studentGroup = studentGroups.get(0);

            PaymentHistory paymentHistory = new PaymentHistory(depositDate, depositorName, bankName, depositAmount, memo, studentGroup.getId());
            paymentHistories.add(paymentHistory);

        } else {
            StudentGroup newStudentGroup = StudentGroup.builder()
                    .groupName("중등 A반")
                    .managerId(userId)
                    .tuition(300000)
                    .totalStudentCount(20)
                    .build();
            studentGroupRepository.save(newStudentGroup);

            PaymentHistory paymentHistory = new PaymentHistory(depositDate, depositorName, bankName, depositAmount, memo, newStudentGroup.getId());
            paymentHistories.add(paymentHistory);
        }
    }
}
