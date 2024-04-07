package com.edubill.edubillApi.payment.service;

import com.edubill.edubillApi.domain.Academy;
import com.edubill.edubillApi.domain.PaymentInfo;
import com.edubill.edubillApi.domain.StudentGroup;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.error.exception.StudentGroupNotFoundException;
import com.edubill.edubillApi.error.exception.UserNotFoundException;

import com.edubill.edubillApi.user.repository.StudentGroupRepository;
import com.edubill.edubillApi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentListCreationService {


    private final StudentGroupRepository studentGroupRepository;

    public void createPaymentInfoList(String formattedDateTime, String depositorName, String bankName, int depositAmount, String memo, List<PaymentInfo> paymentInfoList, String userId) {


        List<StudentGroup> studentGroups = studentGroupRepository.getUserGroupsByUserId(userId);

        if (studentGroups != null && !studentGroups.isEmpty()) {
            //TODO: StudentGroupId에 대한 정보를 받고 해당하는 Id에 따라 paymentInfo를 저장하도록 수정
            StudentGroup studentGroup = studentGroups.get(0);

            PaymentInfo paymentInfo = new PaymentInfo(formattedDateTime, depositorName, bankName, depositAmount, memo, studentGroup.getId());
            paymentInfoList.add(paymentInfo);

        } else {
            StudentGroup newStudentGroup = StudentGroup.builder()
                    .groupName("중등 A반")
                    .managerId(userId)
                    .tuition(300000)
                    .totalStudentCount(20)
                    .build();
            studentGroupRepository.save(newStudentGroup);

            PaymentInfo paymentInfo = new PaymentInfo(formattedDateTime, depositorName, bankName, depositAmount, memo, newStudentGroup.getId());
            paymentInfoList.add(paymentInfo);
        }
    }
}
