package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.UserDto;
import com.edubill.edubillApi.dto.user.JoinRequestDto;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.exception.LoginFailedException;
import com.edubill.edubillApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

//@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserDto join(JoinRequestDto joinRequestDto) {
        return null;
    }

    @Transactional
    public User login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserEmail(loginRequestDto.getUserEmail())
                .orElseThrow(() -> new LoginFailedException("해당 이메일을 가진 사용자가 없다."));

        // 암호화후 db저장된 password와 사용자가 입력한 raw password를 비교
        log.info("encoded_password_entity= {}", user.getUserPassword());
        String encodePassword = passwordEncoder.encode(loginRequestDto.getUserPassword());
        log.info("encoded_password = {}", encodePassword);
        log.info("encoded_password_dto = {}", loginRequestDto.getUserPassword());

        if (!passwordEncoder.matches(loginRequestDto.getUserPassword(), user.getUserPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않는다.");
        }
        return user;
    }

    @Transactional
    public void logout() {

    }

    /**
     * 인증번호 생성 및 전송 서비스
     */

    //인증번호 생성 메서드
    public String generateVerificationCode() {
        // 여기에서 랜덤한 인증번호를 생성하는 로직을 추가
        // 예를 들면, Random 클래스를 사용하여 6자리 숫자 생성
        return String.format("%06d", new Random().nextInt(1000000));
    }

    // 인증번호 전송 메서드
    public void sendVerificationCode(String phoneNumber, String verificationCode) {
        // 여기에서 실제로 전화번호로 인증번호를 발송하는 로직을 추가
        // SMS, 이메일, 앱 푸시 등 다양한 방법을 사용할 수 있음
    }

    /**
     * 회원가입 및 인증 서비스
     */
    // 회원가입 메서드
/*    public void registerMember(String phoneNumber) {
        // 회원 정보 저장 전에 인증번호를 생성하고 전송
        String verificationCode = generateVerificationCode();
        sendVerificationCode(phoneNumber, verificationCode);

        // 회원 정보를 저장하기 전에 인증번호와 함께 저장
        User member = new User();
        member.setPhoneNumber(phoneNumber);
        member.setVerificationCode(verificationCode);
        userRepository.save(member);
    }*/

    // 인증 확인 메서드
/*    public boolean verifyCode(String phoneNumber, String enteredCode) {
        // 전화번호에 해당하는 회원의 저장된 인증번호를 가져옴
        User user = UserRepository.findByPhoneNumber(phoneNumber);

        // 회원이 존재하고, 입력된 인증번호가 저장된 인증번호와 일치하면 인증 성공
        return user != null && user.getVerificationCode().equals(enteredCode);
    }*/
}
