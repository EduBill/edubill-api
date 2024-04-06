package com.edubill.edubillApi.user.repository;

import com.edubill.edubillApi.domain.StudentGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.edubill.edubillApi.domain.QStudentGroup.studentGroup;

@Repository
public class StudentGroupCustomRepositoryImpl implements StudentGroupCustomRepository {

    public StudentGroupCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StudentGroup> getUserGroupsByUserId(String managerId) {
        return queryFactory
                .selectFrom(studentGroup)
                .where(studentGroup.managerId.eq(managerId))
                .fetch();
    }
}