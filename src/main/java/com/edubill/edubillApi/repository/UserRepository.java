package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        String jpql = "select u from User u where u.phoneNumber = :phoneNumber";
        Query query = em.createQuery(jpql, User.class);
        query.setParameter("phoneNumber", phoneNumber);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        User findUser = em.find(User.class, id);
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
}