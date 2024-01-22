package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void memberTest() {
        //given
        User user = new User();
        user.setUsername("memberA");

        //when
        User savedMember = userRepository.save(user);

        //then
        User findUser = userRepository.findById(savedMember.getId()).get();
        assertThat(findUser).isEqualTo(savedMember);
    }
}