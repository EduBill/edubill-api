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
    public void userTest() {
        //given
        User user = new User();
        user.setUserName("memberA");

        //when
        userRepository.save(user);

        //then
       /* User findUser = userRepository.findById(savedMember.getId()).get();
        assertThat(findUser).isEqualTo(savedMember);*/
    }
}