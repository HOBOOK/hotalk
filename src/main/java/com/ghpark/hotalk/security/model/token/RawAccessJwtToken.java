package com.ghpark.hotalk.security.model.token;

import com.ghpark.hotalk.security.exceptions.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Base64;

/**
 * @author pkh879
 */
@Log4j2
@AllArgsConstructor
public class RawAccessJwtToken implements JwtToken{

    @Getter private String token;

    /**
     * JWT 토큰 서명을 분석하고 유효성을 검증
     * @param signingKey
     * @return
     */
    public Jws<Claims> parseClaims(String signingKey){
        try{
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex){
            log.error("JWT 토큰이 적합하지 않습니다.");
            throw new BadCredentialsException("JWT 토큰이 적합하지 않습니다 : ", ex);
        } catch (ExpiredJwtException ex){
            log.info("JWT 토큰이 만료되었습니다.");
            throw new JwtExpiredTokenException(this, "JWT 토큰이 만료되었습니다.", ex);
        }
    }

    public String getSub(){
        String sub = new String(Base64.getDecoder().decode(this.token.split("\\.")[1]));
        return sub.substring(sub.indexOf("sub")+6, sub.indexOf("scopes")-3);
    }

}
