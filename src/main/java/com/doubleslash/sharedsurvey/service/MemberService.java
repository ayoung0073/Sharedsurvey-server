package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.MemberRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    @Transactional
    public boolean register(MemberRequestDto requestDto){
        if(memberRepository.findByMemberId(requestDto.getMemberId()).isPresent()){            return false;
        }
        else{
            Member member = new Member(requestDto);
            memberRepository.save(member);
            return true;
        }
    }
}
