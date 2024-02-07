package com.edubill.edubillApi.jwt;

import com.edubill.edubillApi.dto.SecurityExceptionDto;
import com.edubill.edubillApi.repository.UserRefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRefreshTokenRepository refreshTokenMap;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String accessToken = jwtProvider.resolveToken(request);

        if (accessToken != null) {
            String blackList = refreshTokenMap.getBlackList(accessToken); //?
            if (blackList != null) {
                if (blackList.equals("logout")) {
                    throw new IllegalArgumentException("다시 로그인 해야함.");
                }
            }
            if(!jwtProvider.validateToken(accessToken)){
                response.sendError(401, "만료되었습니다.");
                jwtExceptionHandler(response,"401", HttpStatus.BAD_REQUEST.value());
                return;
            }
            // 검증 후 인증 객체 생성하여 securityContextHolder에서 관리
            Claims userInfo = jwtProvider.getUserInfoFromToken(accessToken);
            String phoneNumber = userInfo.getSubject();
            setAuthentication(phoneNumber);//subject = phoneNumber
        }
        filterChain.doFilter(request,response);
    }

    //세션에 사용자 등록
    private void setAuthentication(String phoneNumber) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtProvider.createUserAuthentication(phoneNumber);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String message, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, message));
            //, ObjectMapper를 사용하여 SecurityExceptionDto 객체를 JSON 문자열로 변환
            response.getWriter().write(json); //JSON 문자열을 응답으로 작성
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}