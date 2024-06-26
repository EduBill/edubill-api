package com.edubill.edubillApi.config.security;

import com.edubill.edubillApi.error.AccessDeniedHandlerCustom;
import com.edubill.edubillApi.error.AuthenticationEntryPointCustom;
import com.edubill.edubillApi.jwt.JwtAuthFilter;
import com.edubill.edubillApi.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationEntryPointCustom authenticationEntryPointCustom;
    private final AccessDeniedHandlerCustom accessDeniedHandlerCustom;

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
                        .requestMatchers("/","/webjars/**","/v1/auth/**","/v1/auth/signup","/v1/auth/login","/error",
                               "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/api-docs/**", "/health"
                                ).permitAll()
                        // Springdoc-openapi 도입 시, Swagger 리소스로의 접근이 불가능해지기 때문에 이와 관련된 엔드포인트들은 Spring security 로직을 타지 않도록 구성필요
                        .anyRequest().authenticated());

        http.
                exceptionHandling(authenticationManager-> authenticationManager
                        .authenticationEntryPoint(authenticationEntryPointCustom) // 401 Error 처리, 인증과정에서 실패할 시 처리
                        .accessDeniedHandler(accessDeniedHandlerCustom)); // 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우

        http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}