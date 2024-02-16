package com.edubill.edubillApi.exception;

import com.edubill.edubillApi.dto.SecurityExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccessDeniedHandlerCustom implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private static final SecurityExceptionDto exceptionDto
            = new SecurityExceptionDto("권한이 없는 사용자 요청", HttpStatus.FORBIDDEN.value());

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String json = objectMapper.writeValueAsString(exceptionDto);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}