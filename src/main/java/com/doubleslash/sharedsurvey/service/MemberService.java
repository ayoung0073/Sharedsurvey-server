package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.config.security.user.Role;
import com.doubleslash.sharedsurvey.domain.dto.member.MemberRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Member idCheck(String memberId){
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
        return optionalMember.orElse(null);
    }

    @Transactional
    public void createMember(MemberRequestDto requestDto){
        memberRepository.save(Member.builder()
                .age(requestDto.getAge())
                .gender(requestDto.isGender())
                .name(requestDto.getName())
                .role(Role.MEMBER)// 최초 가입시 MEMBER 로 설정
                .memberId(requestDto.getMemberId())
                .password(requestDto.getPassword())
                .point(20) // 기본 포인트 20 지급
                .build());
    }
}


