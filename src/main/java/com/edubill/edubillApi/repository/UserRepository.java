package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.UserDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user){
        em.persist(user);
    }
    public void update(Long userId, UserDto userDto) {
        User findUser = em.find(User.class, userId);
        findUser.setUserName(userDto.getUserName());
        findUser.setUserEmail(userDto.getUserEmail());
        findUser.setPhoneNumber(userDto.getPhoneNumber());
    }

    public Optional<User> findById(Long userId){
        User findUser = em.find(User.class, userId);
        return Optional.ofNullable(findUser);
    }

    public void deleteById(Long userId) {
        User findUser = em.find(User.class, userId);
        if (findUser != null) {
            em.remove(findUser);
        }
    }
}