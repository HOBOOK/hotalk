package com.ghpark.hotalk.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghpark.hotalk.model.ErrorCode;
import com.ghpark.hotalk.model.ErrorResponse;
import com.ghpark.hotalk.security.auth.jwt.extractor.TokenExtractor;
import com.ghpark.hotalk.security.config.WebSecurityConfig;
import com.ghpark.hotalk.security.exceptions.AuthMethodNotSupportedException;
import com.ghpark.hotalk.security.exceptions.JwtExpiredTokenException;
import com.ghpark.hotalk.security.model.token.RawAccessJwtToken;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증 실패시 실행 되는 콜백
 * @author pkh879
 */
@Component
@Log4j2
@AllArgsConstructor
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    ObjectMapper mapper;
    TokenExtractor tokenExtractor;

    // 토큰 인증 실패시 오류 메시지 리턴
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        if (ex instanceof BadCredentialsException) {
            mapper.writeValue(response.getWriter(), ErrorResponse.of("사용자 정보가 일치 하지 않음", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        } else if (ex instanceof JwtExpiredTokenException) {
            String tokenPayload = request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME);
            RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));
            mapper.writeValue(response.getWriter(), ErrorResponse.of("만료된 토큰", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));


        } else if (ex instanceof AuthMethodNotSupportedException) {
            mapper.writeValue(response.getWriter(), ErrorResponse.of(ex.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        }

        mapper.writeValue(response.getWriter(), ErrorResponse.of("인증 실패", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
    }
}
