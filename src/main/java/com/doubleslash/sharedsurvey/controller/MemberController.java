package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.domain.dto.MemberRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public List<Member> getMembers(){
        return memberService.findAll();
    }

    @PostMapping("/member")
    public Member createMember(@RequestBody MemberRequestDto requestDto){
        if(memberService.register(requestDto)){
            Member member = new Member(requestDto);
            return member;
        }
        else {
            return null;
        }
    }

}
