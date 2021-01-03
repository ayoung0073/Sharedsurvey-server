package com.doubleslash.sharedsurvey.utils;

import com.doubleslash.sharedsurvey.domain.dto.google.GoogleRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.QuestionChoiceRepository;
import com.doubleslash.sharedsurvey.repository.QuestionRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import com.doubleslash.sharedsurvey.repository.SurveyWriterRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GoogleSurveyParsing {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final SurveyWriterRepository surveyWriterRepository;
    private final QuestionChoiceRepository questionChoiceRepository;

    @Transactional
    public boolean registerSurvey(String url, GoogleRequestDto requestDto, MultipartFile[] files, Member member) throws IOException {
        System.out.println(url);
        Google google = parsing(url);

        String baseDir = "/tmp/tomcat.8080.9056073029680764243/work/Tomcat/localhost/ROOT/";

        String filePath;
        String filename;

        int i = 0;

        requestDto.setWriter(member);

        Survey survey = surveyRepository.save(new Survey(google, requestDto));

        if(survey.isExistFile()){
            filename = files[i].getOriginalFilename();
            assert filename != null;
            if(filename.split("\\.")[1].equalsIgnoreCase("png")) {
                filePath = baseDir + "/" + "thumbnail" + survey.getId() + ".png";//files[i].getOriginalFilename();
                survey.setFilename("thumbnail" + survey.getId() + ".png");
                files[i++].transferTo(new File(filePath));
                surveyRepository.save(survey);
            }
            else if(filename.split("\\.")[1].equalsIgnoreCase("jpg") || (filename.split("\\.")[1].equalsIgnoreCase("jpeg"))){
                filePath = baseDir + "/" + "thumbnail" + survey.getId() + ".jpg";//files[i].getOriginalFilename();
                survey.setFilename("thumbnail" + survey.getId() + ".jpg");
                files[i++].transferTo(new File(filePath));
                surveyRepository.save(survey);
            }
        }

        surveyWriterRepository.save(new SurveyWriter(survey.getId(),member.getId()));
        List<GoogleQuestion> questions = google.getGoogleQuestions();
        for (GoogleQuestion s : questions) {
            s.setSurvey(survey);
            Question question = questionRepository.save(new Question(s));
            if(s.getCategory() == 1 || s.getCategory() == 2){
                // 질문 카테고리가 객관식, 체크박스, 드롭다운일 때
                for(i = 0; i < s.getChoiceTexts().length; i++){
                    QuestionChoice questionChoice = new QuestionChoice(survey, question, s.getChoiceTexts()[i]);
                    questionChoiceRepository.save(questionChoice);
                }
            }
        }
        return true;
    }

    public Google parsing(String url) throws IOException {
        Google google = new Google();

        Document doc = Jsoup.connect(url).get();
        Element element = doc.selectFirst(".freebirdFormviewerViewHeaderTitleRow"); // 설문조사 이름 select

        google.setName(element.text());

        element = doc.selectFirst(".freebirdFormviewerViewHeaderDescription"); // 설문조사 설명 select
        google.setDescription(element.text());

        Elements boxElements = doc.select(".freebirdFormviewerViewNumberedItemContainer"); // 각 질문 container


        List<GoogleQuestion> googleQuestionList = new ArrayList<>();
        for (Element e : boxElements) {

            // 질문 내용 & 필수항목 데이터

            GoogleQuestion googleQuestion = new GoogleQuestion();
            String questionText = e.selectFirst(".freebirdFormviewerComponentsQuestionBaseTitle").text(); // 질문내용 select

            if (questionText.charAt(questionText.length() - 1) == '*') { // 질문텍스트의 마지막 문자가 '*'이면 필수 문항에 해당
                int len = questionText.length() - 1;
                googleQuestion.setRequired(true);
                googleQuestion.setQuestionText(questionText.substring(0, len));
            }
            else googleQuestion.setQuestionText(questionText);


            // 질문 카테고리 데이터

            int category = 0;
            //  라디오 버튼 클래스명
            if (e.getElementsByClass("freebirdFormviewerViewItemsRadiogroupRadioGroup").size() > 0) category = 1;
            //  체크박스 클래스명
            else if (e.getElementsByClass("freebirdFormviewerComponentsQuestionCheckboxRoot").size() > 0) category = 2;
            //  텍스트 클래스명
            else if (e.getElementsByClass("freebirdFormviewerComponentsQuestionTextRoot").size() > 0) category = 5;

            else category = 5;
            googleQuestion.setCategory(category);

            // 서브 질문 데이터

            Elements elements = e.select(".docssharedWizToggleLabeledPrimaryText"); // 서브 질문 텍스트 뽑기
            String[] subChoices = new String[elements.size()]; // 서브 질문 개수만큼 배열 선언
            int num = 0;
            for (Element eSub : elements) {
                subChoices[num++] = eSub.text();
            }

            googleQuestion.setChoiceTexts(subChoices);

            googleQuestionList.add(googleQuestion);

            google.setGoogleQuestions(googleQuestionList);
        }

        System.out.println(google.getName());
        System.out.println(google.getDescription());
        for (int i = 0; i < google.getGoogleQuestions().size(); i++) {
            System.out.println("==============================================================================================================================================================================================");
            GoogleQuestion googleQuestion = googleQuestionList.get(i);
            System.out.println(googleQuestion.questionText);
            System.out.println("카테고리 : " + googleQuestion.category);
            System.out.println(googleQuestion.required ? "(필수)" : "(선택)");
            for (int j = 0; j < googleQuestion.getChoiceTexts().length; j++)
                System.out.println("    " + googleQuestion.choiceTexts[j]);
        }
        return google;
    }
}
