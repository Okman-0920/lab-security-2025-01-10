package com.ll.security_2025_01_10.global.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.security_2025_01_10.domain.member.member.entity.Member;
import com.ll.security_2025_01_10.domain.member.member.service.MemberService;
import com.ll.security_2025_01_10.global.rq.Rq;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
	private final MemberService memberService;
	private final Rq rq;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// /api/로 시작하는 요청이 아니라면 패스
		// 일반적인 /home, /about 등 굳이 인증을 하지 않아도 돌아가도 되는 사이트는 그냥 지나치라는 것임
		if (!request.getRequestURI().contains("/api/")) {
			filterChain.doFilter(request, response);
			return;
		}

		String authorization = request.getHeader("Authorization");

		// Authorization 헤더가 없거나 Bearer 로 시작하지 않는다면 패스
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String apiKey = authorization.substring("Bearer ".length());

		Optional<Member> opMember = memberService.findByApiKey(apiKey);

		if (opMember.isEmpty()) {
			filterChain.doFilter(request, response);
			return;
		}

		Member member = opMember.get();

		rq.setLogin(member.getUsername());

		filterChain.doFilter(request, response);
	}
}
