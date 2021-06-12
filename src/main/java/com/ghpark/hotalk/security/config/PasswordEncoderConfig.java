package com.ghpark.hotalk.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 비밀번호 인코딩 설정
 * @author pkh879
 */
@Configuration
public class PasswordEncoderConfig {
    @Bean
    protected BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
