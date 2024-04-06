package com.edubill.edubillApi.payment.service;

import com.edubill.edubillApi.domain.PaymentInfo;
import com.edubill.edubillApi.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentManagementService {


    private final PaymentRepository paymentRepository;

    public void savePaymentInfos(List<PaymentInfo> paymentInfos) {
        paymentRepository.saveAll(paymentInfos);
    }



}
