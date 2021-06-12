package com.ghpark.hotalk.security.exceptions;

import com.ghpark.hotalk.security.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

/**
 * 만료된 토큰 예외
 * @author pkh879
 */
public class JwtExpiredTokenException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;

    private JwtToken token;

    public JwtExpiredTokenException(String msg){
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t){
        super(msg, t);
        this.token = token;
    }

    public String token(){
        return this.token.getToken();
    }
}
