package com.edubill.edubillApi.repository.group;

import com.edubill.edubillApi.domain.Group;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.edubill.edubillApi.domain.QGroup.group;


@Repository
public class GroupCustomRepositoryImpl implements GroupCustomRepository {

    public GroupCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Group> getGroupsByUserId(String managerId) {
        return queryFactory
                .selectFrom(group)
                .where(group.managerId.eq(managerId))
                .fetch();
    }
}