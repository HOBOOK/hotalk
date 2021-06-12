package com.ghpark.hotalk.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * REST 에러 공통 코드
 * @author pkh879
 */
public enum  ErrorCode {
    GLOBAL(1), // 전역 오류
    AUTHENTICATION(10), // 인증 실패
    JWT_TOKEN_EXPIRED(11), // 토큰 만료
    FAIL_CREATE(101), // 존재 하지 않는 계정 조회
    FAIL_READ_NOT_EXIST(111), // 존재 하지 않는 데이터 조회
    FAIL_UPDATE(121), // 존재 하지 않는 계정 수정
    FAIL_DELETE_NOT_EXIST(131); // 존재 하지 않는 계정 삭제



    private int errorCode;

    private ErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode(){
        return errorCode;
    }

}
