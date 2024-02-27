package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.dto.user.*;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;

import com.edubill.edubillApi.error.ErrorResponse;
import com.edubill.edubillApi.error.exception.BusinessException;
import com.edubill.edubillApi.jwt.JwtProvider;
import com.edubill.edubillApi.jwt.JwtToken;
import com.edubill.edubillApi.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.edubill.edubillApi.error.ErrorCode.INVALID_VERIFY_NUMBER;


@Slf4j
@Tag(name = "Auth", description = "인증관리 API")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    public AuthController(JwtProvider jwtProvider, @Qualifier("authServiceImpl")AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }


    // 인증번호 발송 API
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = VerificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "휴대폰 번호 양식이 맞지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "인증번호 발송", description = "휴대폰 번호를 통해 인증번호를 발급받는다.")
    @PostMapping("/phone")
    public ResponseEntity<VerificationResponseDto> sendVerificationNumber(@Schema(description = "휴대폰번호", type = "String", example = "01012345678") @RequestBody String phoneNumber) {

        VerificationResponseDto verificationResponseDto = authService.sendVerificationNumber(phoneNumber);
        log.info("requestId = {}, 인증번호 ={}", verificationResponseDto.getRequestId(),verificationResponseDto.getVerificationNumber());
        return ResponseEntity.ok(verificationResponseDto);
    }

    // 인증번호 확인 API
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(description = "requestId", type = "String", example = "5cdf2372-d3c2-42ac-b517-0bad88fcbbf7"))),
            @ApiResponse(responseCode = "400", description = "binding error가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))), // @Valided 에 걸리는 경우
            @ApiResponse(responseCode = "401", description = "인증번호가 일치하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))), // validNumber == false 일 경우
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "인증번호 확인", description = "발급받은 인증번호를 입력한 것을 확인한다.")
    @PostMapping("/verify")
    public ResponseEntity<String> verifyNumber(@Validated @RequestBody VerificationRequestDto verificationRequestDto) {

        String requestId = verificationRequestDto.getRequestId();
        String verificationNumber = verificationRequestDto.getVerificationNumber();

        Boolean validNumber = authService.verifyNumber(requestId, verificationNumber);
        if (validNumber) {
            return ResponseEntity.ok(requestId);
        }
        throw new BusinessException("인증번호가 일치하지 않음", INVALID_VERIFY_NUMBER);
    }

    // 사용자 유무 확인 API
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1. 사용자가 존재합니다.(true) \t\n 2. 사용자가 존재하지 않습니다.(false)",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "사용자 유무", description = "가입된 사용자가 존재하는지 확인한다.")
    @PostMapping("/exists/user")
    public ResponseEntity<Boolean> isExistsUser(@Validated @RequestBody ExistUserRequestDto existUserRequestDto) {

        String phoneNumber = existUserRequestDto.getPhoneNumber();
        String requestId = existUserRequestDto.getRequestId();
        log.info("request_id = {}", requestId);

        if (authService.isExistsUser(phoneNumber)) {
            return ResponseEntity.ok(true);
        }
        // 기존회원이 없기때문에 새로 회원을 가입시킬 수 있음.
        return ResponseEntity.ok(false);
    }

    // 회원가입 API
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "binding error가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "회원가입 오류.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원가입", description = "회원이 존재하지 않을 경우 회원가입을 진행한다.")
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        UserDto signupUser = authService.signUp(signupRequestDto);
        return ResponseEntity.ok(signupUser);
    }

    // 로그인 API
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "binding error가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 오류.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "로그인", description = "회원이 존재할 경우 그 회원으로 로그인을 진행한다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UserDto loginUser = authService.login(loginRequestDto);
        JwtToken token = jwtProvider.createTokenByLogin(loginUser.getPhoneNumber(), loginUser.getUserRole());
        response.addHeader(JwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());// 헤더에 access token 만 싣기

        LoginResponseDto loginResponseDto = new LoginResponseDto(token, loginUser);
        return ResponseEntity.ok(loginResponseDto);
    }
}