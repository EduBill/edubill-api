package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.repository.users.UserRepositoryInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepositoryInterface userRepositoryInterface;

    public UserService(UserRepositoryInterface userRepositoryInterface) {
        this.userRepositoryInterface = userRepositoryInterface;
    }

    @Transactional(readOnly = true)
    public User findUser(String userId) {
        return userRepositoryInterface.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User not found. userId: " + userId));
    }
}
