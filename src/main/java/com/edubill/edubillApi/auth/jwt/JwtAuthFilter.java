package com.edubill.edubillApi.auth.jwt;

import com.edubill.edubillApi.auth.dto.SecurityExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final UserDetailsService userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String accessToken = jwtProvider.resolveToken(request);

        if (accessToken != null) {
            if (!jwtProvider.validateToken(accessToken)) {
                jwtExceptionHandler(response, "AccessToken이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            // 검증 후 인증 객체 생성하여 securityContextHolder에서 관리
            Claims userInfo = jwtProvider.getUserInfoFromToken(accessToken);
            String phoneNumber = userInfo.getSubject();
            setAuthentication(phoneNumber);
        }
        filterChain.doFilter(request, response);
    }

    //세션에 사용자 등록
    private void setAuthentication(String phoneNumber) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createUserAuthentication(phoneNumber);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String message, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // ObjectMapper를 사용하여 SecurityExceptionDto 객체를 json 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(new SecurityExceptionDto(message,statusCode));
            response.getWriter().write(json); //json 문자열을 응답으로 작성
        } catch (IOException e) {
            throw new RuntimeException("Error while processing JSON", e);
        }
    }

    public Authentication createUserAuthentication(String phoneNumber) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(phoneNumber);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}