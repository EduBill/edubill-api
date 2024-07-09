package com.edubill.edubillApi.repository.payment;

import java.util.List;

public interface PaymentKeyCustomRepository {
    long deleteByStudentIds(List<Long> studentIds);
}
