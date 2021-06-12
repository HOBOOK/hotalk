package com.ghpark.hotalk.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Response 에러 모델
 * @author pkh879
 */
@Getter
public class ErrorResponse {
    // Http response 상태 코드
    private final HttpStatus status;

    // 에러 메시지
    private final String message;

    // 에러 코드
    private final ErrorCode errorCode;

    private final Date timestamp;

    protected ErrorResponse(final String message, final ErrorCode errorCode, HttpStatus status){
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = new Date();
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode, HttpStatus httpStatus){
        return new ErrorResponse(message, errorCode, httpStatus);
    }
}
