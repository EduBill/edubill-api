package com.edubill.edubillApi.config;

import com.edubill.edubillApi.exception.CustomAccessDeniedHandler;
import com.edubill.edubillApi.exception.CustomAuthenticationEntryPoint;
import com.edubill.edubillApi.repository.UserRefreshTokenRepository;
import com.edubill.edubillApi.jwt.JwtAuthFilter;
import com.edubill.edubillApi.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRefreshTokenRepository refreshTokenMap;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(CsrfConfigurer::disable);
        http
                .formLogin(FormLoginConfigurer::disable);
        http
                .httpBasic(HttpBasicConfigurer::disable);
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/v1/auth/**","/v1/auth/api/signup","/v1/auth/api/login").permitAll()
                        .anyRequest().authenticated());

        http.
                exceptionHandling(authenticationManager-> authenticationManager
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 401 Error 처리, 인증과정에서 실패할 시 처리
                        .accessDeniedHandler(customAccessDeniedHandler)); // 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우

        http
                .addFilterBefore(new JwtAuthFilter(refreshTokenMap, jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}