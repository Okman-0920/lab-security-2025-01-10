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
		http
			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests // 승인 요청시
					.requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts", "/api/*/posts/{postId:\\d+}/comments", "/h2-console/**")
					.permitAll()
					// Matcher 된 get 매서드는 승인한다
					.anyRequest()
					// 그 외에 나머지 요청은
					.authenticated()
			)
				// 인증되어야만 한다
			.headers(
				headers ->
					headers.frameOptions(
						frameOptions -> frameOptions.sameOrigin()
					)

			).csrf(csrf ->
				csrf.disable()
			// 보통 restAPI에서 csrf는 끈다
			);

		return http.build();
	}
}
