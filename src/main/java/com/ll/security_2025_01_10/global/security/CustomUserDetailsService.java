package com.ll.security_2025_01_10.global.security;

import com.ll.security_2025_01_10.domain.member.member.entity.Member;
import com.ll.security_2025_01_10.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// 사용자명으로 사용자 정보를 조회하여 UserDetails 객체로 변환하는 역할
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new User( // 시큐리티는 회원 구조를 모르기 때문에 담아서 리턴해줘야함
                member.getUsername(),
                member.getPassword(),
                List.of()
        );
    }
}