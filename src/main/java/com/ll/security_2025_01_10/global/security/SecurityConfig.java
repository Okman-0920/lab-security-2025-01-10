package com.ll.security_2025_01_10.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean // 기존 시큐리티의 기능을 다 버리고, 우리의 커스터마이징 기능을 사용
    public SecurityFilterChain baseSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                // 인증을 요청한다
                authorizeRequests
                        // request 를 matchers(승인) 할지 말지를 결정해라
                        .requestMatchers("/h2-console/**")
                        // authenticated() 와 충돌하나 조금 더 상위의 룰이기 때문에 All() 을 적용
                        // 룰은 위에서부터 (위쪽 코드부터) 적용
                        .permitAll()
                        .requestMatchers("/h2-console/login.do")
                        .authenticated()
                        .requestMatchers(
                                // HTTP GET 요청에 대한 URL패턴은
                                HttpMethod.GET,
                                // {id\\d+} = id는 아무거나 적어도 상관 없음 \\d+ 는 10진수 정수 패턴을 의미
                                "/api/*/posts/{id:\\d+}",
                                "/api/*/posts",
                                "/api/*/posts/{postId:\\d+}/comments")


                        .permitAll()
                        // 그 외에 다른 요청은
                        .anyRequest()

                        // 인증 해야만 한다
                        .authenticated()
        ).headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf( // 보통 restApi에서는 끈다
                        csrf ->
                                csrf.disable()
                );
        return http.build();
    }
}

