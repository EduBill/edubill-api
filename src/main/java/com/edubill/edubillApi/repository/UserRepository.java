package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.UserUpdateRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
    public void update(Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        User findUser = em.find(User.class, userId);
        findUser.setUserName(userUpdateRequestDto.getUserName());
        findUser.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        User findUser = em.find(User.class, phoneNumber);
        return Optional.ofNullable(findUser);
    }

//    public Optional<User> findByUserEmail(String userEmail){
//        User findUser = em.find(User.class, userEmail);
//        return Optional.ofNullable(findUser);
//    }

    public Optional<User> findByUserName(String userName){
        User findUser = em.find(User.class, userName);
        return Optional.ofNullable(findUser);
    }

    public void deleteByRequestId(String requestId) {
        User findUser = em.find(User.class, requestId);
        if (findUser != null) {
            em.remove(findUser);
        }
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        Query query = em.createQuery("select count(u) from User u where u.phoneNumber = :phoneNumber");
        query.setParameter("phoneNumber", phoneNumber);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    public boolean existsByRequestId(String requestId) {
        Query query = em.createQuery("select count(u) from User u where u.requestId = :requestId");
        query.setParameter("requestId", requestId);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }


}