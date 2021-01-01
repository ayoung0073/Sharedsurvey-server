package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Point;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import com.doubleslash.sharedsurvey.repository.PointRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;

    public int getPoint(Long surveyId, int point, Member member)  {
        Optional<Survey> survey = surveyRepository.findById(surveyId);
        member.getPoint(survey.orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")).getPoint());
        memberRepository.save(member);

        pointRepository.save(new Point(member.getId(), point,true, surveyId));
        return member.getPoint();

    }

    public void usePoint(Member member, int point, Long surveyId){
        member.usePoint(point);

        pointRepository.save(new Point(member.getId(), point, false, surveyId));
        memberRepository.save(member);
    }

}
