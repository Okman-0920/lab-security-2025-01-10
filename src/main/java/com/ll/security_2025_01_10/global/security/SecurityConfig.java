package com.ll.security_2025_01_10.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain baseSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                // 인증을 요청한다
                authorizeRequests
                        // request를 matchers(승인) 할지 말지를 결정해라
                        // HTTP GET 요청에 대한 UTL패턴은
                        .requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts", "/api/*/posts/{postId:\\d+}/comments")
                        // {id\\d+} = id는 아무거나 적어도 상관 없음 \\d+ 는 10진수 정수 패턴을 의미

                        // 전부 허용해라
                        .permitAll()

                        // 그 외에 다른 요청은
                        .anyRequest()

                        // 인증되어야만 한다
                        .authenticated()
        );
        return http.build();
    }
}

