package com.ghpark.hotalk.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt토큰 설정 클래스
 * @author pkh879
 */
@Configuration
@Getter
@Setter
public class JwtSettings {

    /**
     * {@link JwtToken}
     * 토큰 만료 시간 30
     */
    private Integer tokenExpirationTime = 30;

    /**
     * 토큰 발행자
     */
    private String tokenIssuer = "ghpark";

    /**
     * {@link JwtToken}
     * 서명할 때 사용되는 키
     */
    private String tokenSigningKey = "hobook";

    /**
     * {@link JwtToken}
     * 재발급 토큰 만료시간 7일
     */
    private Integer refreshTokenExpTime = 60 * 24 * 7;
}
