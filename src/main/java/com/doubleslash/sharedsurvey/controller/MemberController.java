package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.config.security.JwtTokenProvider;
import com.doubleslash.sharedsurvey.config.security.user.CurrentUser;
import com.doubleslash.sharedsurvey.config.security.user.CustomUserDetailService;
import com.doubleslash.sharedsurvey.config.security.user.SecurityMember;
import com.doubleslash.sharedsurvey.domain.dto.LoginRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.MemberRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import com.doubleslash.sharedsurvey.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/member")
    public List<Member> getMembers(){
        return memberService.findAll();
    }

    @PostMapping("/idCheck")
    public Map<String, Boolean> idCheck(@RequestBody MemberRequestDto requestDto){
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", !memberRepository.findByMemberId(requestDto.getMemberId()).isPresent());
        return map;
    }

    @PostMapping("/join")
    public Map<String, Boolean> createMember(@RequestBody MemberRequestDto requestDto){
        Map<String, Boolean> map = new HashMap<>();
        if (memberRepository.findByMemberId(requestDto.getMemberId()).isPresent()) {
            map.put("success", false);
        } else {
            requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            memberRepository.save(Member.builder()
                    .age(requestDto.getAge())
                    .gender(requestDto.isGender())
                    .name(requestDto.getName())
                    .roles(Collections.singletonList("ROLE_Member"))// 최초 가입시 MEMBER 로 설정
                    .memberId(requestDto.getMemberId())
                    .password(requestDto.getPassword())
                    .point(30)
                    .build()).getId();
            map.put("success", true);
        }
        return map;
    }

    // 로그인
    @PostMapping("/login") // Map<String, Object>
    public Map<String, Object> login(@RequestBody LoginRequestDto requestDto) {
          Map<String, Object> map = new HashMap<>();
        if (!memberRepository.findByMemberId(requestDto.getMemberId()).isPresent()) {
            map.put("success", false);
            map.put("response","아이디 존재하지 않습니다.");

        } else {
            Member member = memberRepository.findByMemberId(requestDto.getMemberId()).get();
            if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
                map.put("success", false);
                map.put("response","잘못된 비밀번호입니다.");
            }
            else{
                map.put("success", true);
                map.put("token",jwtTokenProvider.createToken(member.getUsername(), member.getRoles()));
            }
        }
        return map;
    }

    @GetMapping("/checkJWT")
    public String list(@AuthenticationPrincipal Member user){
        //권한체크
        //Authentication user = SecurityContextHolder.getContext().getAuthentication();
        //user.
        //if(user.getPrincipal().equals("anonymousUser")) return "유효하지 않은 토큰";
        if(user == null) return "유효하지 않은 토큰";

        else {
//            Member user2 = (Member) user.getPrincipal();
            //Member user2 = (Member) user.getPrincipal();
            return user.getAuthorities() + " / " + user.getMemberId() + " / " + user.getPassword();
        }
    }

}
