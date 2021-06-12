package com.ghpark.hotalk.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * 요청한 HTTP METHOD가 없을때 발생하는 예외
 * @author pkh879
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    private static final long serialVersionUID = 3705043083010304496L;

    public AuthMethodNotSupportedException(String msg){
        super(msg);
    }
}
