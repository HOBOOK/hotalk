package com.ghpark.hotalk.security.auth.jwt.extractor;


import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;


/**
 * 인가 토큰을 추출하는 클래스
 * @author pkh879
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor{
    private String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header){
        if(StringUtils.isBlank(header)){
            throw new AuthenticationServiceException("인가 헤더는 공백일 수 없습니다.");
        }
        if(header.length() < HEADER_PREFIX.length()){
            throw new AuthenticationServiceException("인가 헤더의 크기가 적합하지 않습니다.");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }

}
