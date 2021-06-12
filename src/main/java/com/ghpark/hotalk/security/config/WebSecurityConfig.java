package com.ghpark.hotalk.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghpark.hotalk.config.CustomCorsFilter;
import com.ghpark.hotalk.security.RestAuthenticationEntryPoint;
import com.ghpark.hotalk.security.auth.ajax.AjaxAuthenticationProvider;
import com.ghpark.hotalk.security.auth.ajax.AjaxLoginProcessingFilter;
import com.ghpark.hotalk.security.auth.jwt.JwtAuthenticationProvider;
import com.ghpark.hotalk.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.ghpark.hotalk.security.auth.jwt.SkipPathRequestMatcher;
import com.ghpark.hotalk.security.auth.jwt.extractor.TokenExtractor;
import com.ghpark.hotalk.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 보안 처리 설정
 * @author pkh879
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/api/auth/login"; // 토큰 발급 URL
    public static final String REFRESH_TOKEN_URL = "/api/auth/token"; // 토큰 재발급 URL
    public static final String RESET_AUTH_URL = "/api/auth/reset"; // 비밀번호 재설정 URL
    public static final String ADMIN_ROOT_URL = "/api/admin/**"; // 관리자 루트 URL
    public static final String API_ROOT_URL = "/api/**"; // Api 루트 URL
    public static final String API_FACTORY_URL = "/api/factory/**"; // 공장 현장 API URL
    public static final String API_FILE_DOWNLOAD_URL = "/api/file/download/**"; // 파일 다운 API URL
    public static final String SOCKET_URL = "/api/ws";
    public static final String SOCKET_INFO_URL = "/api/ws/**";
    public static final String SOCKET_APP_URL = "/api/app/**";
    public static final String SOCKET_TOPIC_URL = "/api/topic/**";

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    @Autowired
    AjaxAuthenticationProvider ajaxAuthenticationProvider;
    @Autowired
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    TokenExtractor tokenExtractor;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ObjectMapper objectMapper;

    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, authenticationSuccessHandler, authenticationFailureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
                = new JwtTokenAuthenticationProcessingFilter(authenticationFailureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(
                AUTHENTICATION_URL,
                REFRESH_TOKEN_URL,
                RESET_AUTH_URL,
                API_FACTORY_URL,
                API_FILE_DOWNLOAD_URL,
                SOCKET_URL,
                SOCKET_INFO_URL,
                SOCKET_APP_URL,
                SOCKET_TOPIC_URL
        );

        http
                .csrf().disable() // JWT 인증방식에는 CSRF가 필요없다.
                .exceptionHandling()
                .authenticationEntryPoint(this.restAuthenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()])) // 모든 접근 가능한 URL 목록
                .permitAll()

                .and()
                .authorizeRequests()
                .antMatchers(API_ROOT_URL).authenticated() // API 루트 경로 인증 설정
                .antMatchers(ADMIN_ROOT_URL).hasAuthority(Role.ADMIN.name()) // ADMIN 루트 권한 설정

                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler) // 권한 예외 핸들링

                .and()
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList,
                        API_ROOT_URL), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(6400000);
        loggingFilter.setAfterMessagePrefix("After request : ");
        return loggingFilter;
    }
}
