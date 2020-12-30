# 5th-final-team10-server

#### API
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
- [GET] /survey/answer/{surveyId} 해당 설문조사 응답 보기
- [GET] /survey?search=~  검색하고자 하는 단어를 포함하는 설문조사 모음
- [GET] /survey/end?search=~ 검색하고자 하는 단어를 포함하는 종료된 설문조사 모음