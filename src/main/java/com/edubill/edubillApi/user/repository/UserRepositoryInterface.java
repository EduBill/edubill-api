package com.edubill.edubillApi.user.repository;

import com.edubill.edubillApi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryInterface extends JpaRepository<User, String> {
}
