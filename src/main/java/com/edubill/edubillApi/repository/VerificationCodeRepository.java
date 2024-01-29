package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, String> {

}
