package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.point.PointInfoDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Point;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import com.doubleslash.sharedsurvey.repository.PointRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public int getPoint(Long surveyId, int point, Member member)  {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> {throw new IllegalArgumentException("해당 설문조사가 없습니다.");});
        member.getPoint(survey.getPoint());
        memberRepository.save(member);

        pointRepository.save(new Point(member, true, survey));
        return member.getPoint();

    }

    @Transactional
    public void usePoint(Member member, Long surveyId){ // 응답 결과 봤을 때
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
            () -> {return new IllegalArgumentException("해당 설문조사가 없습니다.");});
        if(member != survey.getWriter()) { // 설문조사 작성자가 아니면 포인트 삭감
            member.usePoint(survey.getPoint()); // 설문조사에 등록된 포인트만큼
            pointRepository.save(new Point(member, false, survey));
        }
    }

    @Transactional(readOnly = true)
    public List<PointInfoDto> getMyPoints(Long memberId){
        List<PointInfoDto> list = new ArrayList<>();
        for(Point p: pointRepository.findAllByMemberId(memberId)){
            PointInfoDto dto = new PointInfoDto(p.getId(), p.isType(), p.getSurvey().getId(), p.getSurvey().getName(), p.getSurvey().getPoint(), p.getCreatedAt());
            list.add(dto);
        }
        return list;
    }
}
