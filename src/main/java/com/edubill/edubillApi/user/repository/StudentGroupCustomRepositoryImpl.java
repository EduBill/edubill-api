package com.edubill.edubillApi.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.edubill.edubillApi.domain.QStudentGroup.studentGroup;

@Repository
public class StudentGroupCustomRepositoryImpl implements StudentGroupCustomRepository {

    public StudentGroupCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;

    @Override
    public long countUserGroupsByUserId(String managerId) {
        return queryFactory
                .selectFrom(studentGroup)
                .where(studentGroup.managerId.eq(managerId))
                .fetchCount();
    }
}