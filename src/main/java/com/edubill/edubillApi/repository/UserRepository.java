package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public User save(User member){
        em.persist(member);
        return member;
    }
    public Optional<User> findById(Long id){
        User findUser = em.find(User.class, id);
        return Optional.ofNullable(findUser);
    }
}