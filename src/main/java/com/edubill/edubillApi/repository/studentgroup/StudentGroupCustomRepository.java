package com.edubill.edubillApi.repository.studentgroup;

import com.edubill.edubillApi.domain.StudentGroup;

import java.util.List;

public interface StudentGroupCustomRepository {
    List<StudentGroup> getStudentGroupsByUserId(String userId);
}
