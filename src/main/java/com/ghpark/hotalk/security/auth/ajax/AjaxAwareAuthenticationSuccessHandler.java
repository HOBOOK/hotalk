package com.ghpark.hotalk.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghpark.hotalk.security.config.JwtSettings;
import com.ghpark.hotalk.security.model.UserContext;
import com.ghpark.hotalk.security.model.token.AccessJwtToken;
import com.ghpark.hotalk.security.model.token.JwtToken;
import com.ghpark.hotalk.security.model.token.JwtTokenFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 인증 성공시 실행 되는 콜백
 * @author pkh879
 */
@Component
@Log4j2
@AllArgsConstructor
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    ObjectMapper objectMapper;
    JwtTokenFactory jwtTokenFactory;
    JwtSettings jwtSettings;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserContext userContext = (UserContext) authentication.getPrincipal();

        AccessJwtToken accessToken = jwtTokenFactory.createAccessJwtToken(userContext);
        JwtToken refreshToken = jwtTokenFactory.createRefreshToken(userContext);

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("token", accessToken.getToken());
        //redisUtil.setDataExpire(userContext.getUsername(), refreshToken.getToken(), (long)jwtSettings.getRefreshTokenExpTime());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), tokenMap);

        clearAuthenticationAttributes(request);
    }

    /**
     * 저장되어있을 수 있는 임시 인증 관련 데이터를 제거
     *
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
