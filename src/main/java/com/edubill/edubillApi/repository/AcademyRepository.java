package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.Academy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {
    Academy findByUserId(String userId);
}