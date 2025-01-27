package com.ll.security_2025_01_10.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ll.security_2025_01_10.global.rsData.RsData;
import com.ll.security_2025_01_10.global.standard.util.Ut;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomAuthenticationFilter customAuthenticationFilter;

	@Bean
	public SecurityFilterChain baseSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests // 승인 요청시
					.requestMatchers("/h2-console/**")
					.permitAll()
					.requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts", "/api/*/posts/{postId:\\d+}/comments")
					.permitAll()
					.requestMatchers("/api/*/members/login", "/api/*/members/join")
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
			// 보통 restAPI에서 csrf는 끈다
			).csrf(csrf ->
				csrf.disable()
			)
			// addFilterBefore( 여기 필터가, 이 것이 작동하기 전에) 작동시켜라
			.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(
				exceptionHandling -> exceptionHandling
					.authenticationEntryPoint(
						(request, response, authException) -> {
							response.setContentType("application/json;charset=UTF-8");

							// 401체크 체크
							boolean is401 = authException.getLocalizedMessage().contains("authentication is required");

							if (is401) {
								response.setStatus(401);
								response.getWriter().write(
									Ut.json.tostring(
										new RsData("401-1", "사용자 인증정보가 올바르지 않습니다.")
									)
								);
								return;
							}

							response.setStatus(403);
							response.getWriter().write(
								Ut.json.tostring(
									new RsData("403-1", request.getRequestURI() + ", " + authException.getLocalizedMessage())
								)
							);
						}
					)
			);
		;


		return http.build();
	}
}
