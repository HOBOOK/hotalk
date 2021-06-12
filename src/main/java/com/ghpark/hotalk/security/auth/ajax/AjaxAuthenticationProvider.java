package com.ghpark.hotalk.security.auth.ajax;

import com.ghpark.hotalk.security.UserRepository;
import com.ghpark.hotalk.security.model.Role;
import com.ghpark.hotalk.security.model.User;
import com.ghpark.hotalk.security.model.UserContext;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link AuthenticationProvider} 구현
 * Ajax 인증을 수행하는 클래스
 * @author pkh879
 */
@Component
@Log4j2
@AllArgsConstructor
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    BCryptPasswordEncoder encoder;
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        Assert.notNull(authentication, "인증 정보를 받지못했습니다.");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("사용자 정보가 없습니다 : " + username));
        if(!encoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("사용자 인증 정보가 일치하지 않습니다.");
        }
        user.setAuthDate(LocalDateTime.now());
        userRepository.save(user);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        List<GrantedAuthority> authorities = roles.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.authority()))
                .collect(Collectors.toList());
        UserContext userContext = UserContext.create(user.getEmail(), authorities);

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication){
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
