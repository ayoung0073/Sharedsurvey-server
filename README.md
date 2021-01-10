## SharedSurvey

#### API Docs
- [POST] /join 회원가입
- [POST] /idCheck 아이디 중복 체크
- [POST] /login 로그인
- [GET]  /surveys/end ****종료된 설문조사 모음
- [GET] /surveys 진행중 설문조사 모음
- [POST] /survey 설문조사 등록
- [PUT] /survey/{surveyId} 설문조사 수정
- [DELETE] /survey/{surveyId} 해당 설문조사 삭제
- [GET] /survey/{surveyId} 해당 설문조사 보기
- [POST] /survey/{surveyId} 설문조사 응답하기
- [GET] /survey/{surveyId}/answer 해당 설문조사 응답 보기
- [GET] /question/{questionId}/answer/age-gender 해당 질문 나이/성별 별 응답 보기
- [GET] /survey?search=~  검색하고자 하는 단어를 포함하는 설문조사 모음
- [GET] /survey/end?search=~ 검색하고자 하는 단어를 포함하는 종료된 설문조사 모음
- [GET] /survey/{surveyId}/me 해당 설문조사에 참여한 내 답변 보기
- [GET] /myPage 마이페이지 보기
- [POST] /survey/google 구글 설문조사 첨부