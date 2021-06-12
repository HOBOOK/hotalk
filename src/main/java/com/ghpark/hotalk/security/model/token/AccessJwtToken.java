package com.ghpark.hotalk.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;

/**
 * JWT 토큰 표현
 * @author pkh879
 */
@NoArgsConstructor
public final class AccessJwtToken implements JwtToken{
    private String rawToken;
    @JsonIgnore
    private Claims claims;

    protected AccessJwtToken(final String token, Claims claims){
        this.rawToken = token;
        this.claims = claims;
    }

    public String getToken(){
        return this.rawToken;
    }

    public Claims getClaims() {
        return claims;
    }
}
