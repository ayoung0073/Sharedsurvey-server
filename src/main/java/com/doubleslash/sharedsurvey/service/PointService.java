package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Point;
import com.doubleslash.sharedsurvey.domain.entity.SurveyAnswer;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import com.doubleslash.sharedsurvey.repository.PointRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;

    public int getPoint(Long surveyId, Member member){
        member.getPoint(surveyRepository.findById(surveyId).get().getPoint());
        memberRepository.save(member);

        pointRepository.save(new Point(member.getId(), true, surveyId));
        return member.getPoint();
    }

}
