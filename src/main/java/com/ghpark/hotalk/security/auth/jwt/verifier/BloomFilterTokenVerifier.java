package com.ghpark.hotalk.security.auth.jwt.verifier;

import org.springframework.stereotype.Component;

/**
 * @author pkh879
 */

@Component
public class BloomFilterTokenVerifier implements TokenVerifier{
    @Override
    public boolean verify(String jti){
        return true;
    }
}
