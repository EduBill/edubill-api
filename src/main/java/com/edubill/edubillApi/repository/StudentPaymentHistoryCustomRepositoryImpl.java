package com.edubill.edubillApi.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static com.edubill.edubillApi.domain.QStudentPaymentHistory.studentPaymentHistory;

@Repository
@RequiredArgsConstructor
public class StudentPaymentHistoryCustomRepositoryImpl implements StudentPaymentHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Long> findStudentIdsByUserIdAndYearMonth(String userId, YearMonth yearMonth) {
        // 삭제된 paymentHistory에 해당하는 student_id들을 조회
        return queryFactory
                .select(studentPaymentHistory.student.id)
                .from(studentPaymentHistory)
                .where(studentPaymentHistory.paymentHistory.managerId.eq(userId)
                        .and(studentPaymentHistory.yearMonth.eq(String.valueOf(yearMonth))))
                .fetch();
    }
}
