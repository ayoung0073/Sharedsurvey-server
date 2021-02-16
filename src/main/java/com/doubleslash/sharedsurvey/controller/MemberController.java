package com.doubleslash.sharedsurvey.controller;
import com.doubleslash.sharedsurvey.config.security.JwtTokenProvider;
import com.doubleslash.sharedsurvey.domain.dto.member.LoginRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.member.MemberRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.response.SuccessDto;
import com.doubleslash.sharedsurvey.domain.dto.response.SuccessMessageDto;
import com.doubleslash.sharedsurvey.domain.dto.response.SuccessTokenDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/id-check")
    public SuccessDto idCheck(@RequestBody MemberRequestDto requestDto){
        // 기존에 있으면 false
        if(memberService.idCheck(requestDto.getMemberId()) == null) return new SuccessDto(true);
        else return new SuccessDto(false);
    }

    @PostMapping("/join")
    public SuccessDto join(@RequestBody MemberRequestDto requestDto){
        if (memberService.idCheck(requestDto.getMemberId()) != null) {
            return new SuccessDto(false);
        } else {
            requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            memberService.createMember(requestDto);
            return new SuccessDto(true);
        }
    }

    // 로그인
    @PostMapping("/login") // Map<String, Object>
    public Object login(@RequestBody LoginRequestDto requestDto) {
          Member member = memberService.idCheck(requestDto.getMemberId());

        if (member == null)
            return new SuccessMessageDto(false, "해당 아이디가 존재하지 않습니다.");
        else {
            if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
                return new SuccessMessageDto(false, "비밀번호를 확인해주세요");
            }
            else{
                return new SuccessTokenDto(true, jwtTokenProvider.generateToken(member));
            }
        }
    }
}
