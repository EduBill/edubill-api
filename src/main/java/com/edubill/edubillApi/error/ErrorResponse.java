package com.edubill.edubillApi.error;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.ConstraintViolation; //@Valid : java 기술
import org.springframework.validation.BindingResult; //@Validated : spring기술
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.errors = errors;
    }

    private ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode code, final String message) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = message;
        this.errors = new ArrayList<>();
    }

    // BindingResult에 대한 ErrorResponse 객체 생성
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
            return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        public static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    ))
                    .collect(Collectors.toList());
        }
    }
}