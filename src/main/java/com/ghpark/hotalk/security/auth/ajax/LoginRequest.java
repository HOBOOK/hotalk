package com.ghpark.hotalk.security.auth.ajax;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * AJAX 기반 인증에 사용되는 모델
 * @author pkh879
 */
@Getter
@JsonAutoDetect
public class LoginRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("accessInfo")
    private String accessInfo;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.username = username;
        this.password = password;
    }
}
