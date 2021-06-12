package com.ghpark.hotalk.security.auth.jwt;

import com.ghpark.hotalk.security.auth.JwtAuthenticationToken;
import com.ghpark.hotalk.security.config.JwtSettings;
import com.ghpark.hotalk.security.model.UserContext;
import com.ghpark.hotalk.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link AuthenticationProvider} 구현
 * 토큰 인증을 수행하기 위한 인스턴스
 * @author pkh879
 */
@Component
@SuppressWarnings("unchecked")
@Log4j2
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSetting){
        this.jwtSettings = jwtSetting;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{

        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserContext context = UserContext.create(subject, authorities);
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
