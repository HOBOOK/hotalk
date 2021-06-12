package com.ghpark.hotalk.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghpark.hotalk.security.exceptions.AuthMethodNotSupportedException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Ajax 인증 유효성 필터
 * @author pkh879
 */
@Log4j2
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final ObjectMapper objectMapper;

    public AjaxLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, ObjectMapper objectMapper){
        super(defaultProcessUrl);
        this.authenticationSuccessHandler = successHandler;
        this.authenticationFailureHandler = failureHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(!HttpMethod.POST.name().equals(request.getMethod())){
            throw new AuthMethodNotSupportedException("지원 하지 않는 인증 메소드");
        }
        LoginRequest loginRequest;
        if(request.getContentType().toLowerCase().contains("json")){
            loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        }else {
            loginRequest = new LoginRequest(request.getParameter("username"), request.getParameter("password"));
        }
        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("사용자 정보가 모두 입력 되지 않았습니다.");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }
}
