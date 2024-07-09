package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.QPaymentKey;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Repository
@RequiredArgsConstructor
public class PaymentKeyCustomRepositoryImpl implements PaymentKeyCustomRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    @Transactional
    public long deleteByStudentIds(List<Long> studentIds) {

        QPaymentKey paymentKey = QPaymentKey.paymentKey1;

        return queryFactory.delete(paymentKey)
                .where(paymentKey.student.id.in(studentIds))
                .execute();
    }
}
