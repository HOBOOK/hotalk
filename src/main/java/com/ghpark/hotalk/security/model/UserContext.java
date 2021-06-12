package com.ghpark.hotalk.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @author pkh879
 */

@Getter
@NoArgsConstructor
public class UserContext {

    private String username;

    private List<GrantedAuthority> authorities;

    private UserContext(String username, List<GrantedAuthority> authorities){
        this.username = username;
        this.authorities = authorities;
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities){
        if(StringUtils.isBlank(username)) throw new IllegalArgumentException("유저 이름이 공백입니다. " + username);
        return new UserContext(username, authorities);
    }

}
