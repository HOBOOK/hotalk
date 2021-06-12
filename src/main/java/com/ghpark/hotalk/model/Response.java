package com.ghpark.hotalk.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Response 모
 * @author pkh879델
 */
@Getter
public class Response {
    // Http response 상태 코드
    private final HttpStatus status;

    // 에러 메시지
    private final Object data;

    // 에러 코드
    private final String requestURI;

    private final Date timestamp;

    protected Response(final Object data, final String requestURI, HttpStatus status){
        this.data = data;
        this.requestURI = requestURI;
        this.status = status;
        this.timestamp = new Date();
    }

    public static Response of(final Object data, final String requestURI, HttpStatus httpStatus){
        return new Response(data, requestURI, httpStatus);
    }
}
