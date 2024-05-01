package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryInterface extends JpaRepository<User, String> {
}
