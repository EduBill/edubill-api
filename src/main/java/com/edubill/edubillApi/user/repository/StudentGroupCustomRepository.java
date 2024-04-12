package com.edubill.edubillApi.user.repository;

import com.edubill.edubillApi.user.domain.StudentGroup;

import java.util.List;

public interface StudentGroupCustomRepository {
    List<StudentGroup> getUserGroupsByUserId(String userId);
}
