package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

//    @Transactional
//    public boolean register(MemberRequestDto requestDto) {
//        if (memberRepository.findByMemberId(requestDto.getMemberId()).isPresent()) {
//            return false;
//        } else {
//            requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
//            Member member = new Member(requestDto);
//
//            memberRepository.save(member);
//            return true;
//        }
//    }

//    @Transactional
//    public String login(LoginRequestDto requestDto){
//
//        Member member = memberRepository.findByMemberId(requestDto.getMemberId())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다."));
//        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
//
//
////        SecurityMember securityMember = (SecurityMember)loadUserByUsername(requestDto.getMemberId());
////        Optional<Member> member = memberRepository.findByMemberId(securityMember.getUsername());
////        if(!member.isPresent()) return null;
////        else {
////            if (!passwordEncoder.matches(requestDto.getPassword(), member.get().getPassword())) {
////                return null;
////            }
////            return jwtTokenProvider.createToken(securityMember.getUsername(), securityMember.getRoles());
////        }
//    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
//        SecurityMember securityMember = new SecurityMember();
//        if (member != null) {
//            securityMember.setName(member.getName());
//            securityMember.setMemberId(member.getMemberId()); //Principal
//            securityMember.setPassword(member.getPassword()); //credetial
//            securityMember.getRoles().add("ROLE_MEMBER");
//            securityMember.setRoles(securityMember.getRoles());
//        }
//        return securityMember;
//    }
}


