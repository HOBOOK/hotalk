package com.ghpark.hotalk.security.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghpark.hotalk.model.ErrorCode;
import com.ghpark.hotalk.model.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 계정의 권한에 따른 접근 제한 핸들러
 * @author pkh879
 */

@Component
@AllArgsConstructor
public class JwtTokenAccessDeniedHandler implements AccessDeniedHandler {
    ObjectMapper mapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        mapper.writeValue(response.getWriter(), ErrorResponse.of("접근 권한이 없음", ErrorCode.AUTHENTICATION, HttpStatus.NOT_ACCEPTABLE));
    }
}
