package com.edubill.edubillApi.user.repository;

public interface StudentGroupCustomRepository {
    long countUserGroupsByUserId(String userId);
}
