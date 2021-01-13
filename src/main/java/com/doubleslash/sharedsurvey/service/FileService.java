package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.config.security.ApplicationYmlRead;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Question;
import com.doubleslash.sharedsurvey.domain.entity.QuestionChoice;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.QuestionChoiceRepository;
import com.doubleslash.sharedsurvey.repository.QuestionRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {

    private final ApplicationYmlRead applicationYmlRead;
    private final SurveyRepository surveyRepository;
    private final QuestionChoiceRepository questionChoiceRepository;
    private final QuestionRepository questionRepository;


    String filename;
    String filepath;

    @Transactional
    public void saveSurveyFile(Survey survey, MultipartFile[] files) throws Exception {
        String baseDir = applicationYmlRead.getBase_dir();
        //String baseDir = SharedsurveyApplication.class.getResource("").getPath() + "..\\..\\..\\..\\..\\..\\resources\\main\\static\\file";

        int i = 0;

        List<Survey> surveyList = new ArrayList<>();
        if(survey.isExistFile()){
            filename = files[i].getOriginalFilename();
            assert filename != null;
            if(filename.split("\\.")[1].equalsIgnoreCase("png")) {
                filepath = baseDir + "/" + "thumbnail" + survey.getId() + ".png"; //files[i].getOriginalFilename();
                survey.setFilename("thumbnail" + survey.getId() + ".png");
                files[i++].transferTo(new File(filepath));
                surveyList.add(survey);
                //surveyRepository.save(survey);
            }
            else if(filename.split("\\.")[1].equalsIgnoreCase("jpg") || (filename.split("\\.")[1].equalsIgnoreCase("jpeg"))){
                filepath = baseDir + "/" + "thumbnail" + survey.getId() + ".jpg"; //files[i].getOriginalFilename();
                survey.setFilename("thumbnail" + survey.getId() + ".jpg");
                files[i++].transferTo(new File(filepath));
                surveyList.add(survey);
            }
            else throw new Exception("저장할 수 없는 타입의 파일이 있습니다.");
        }
        surveyRepository.saveAll(surveyList);
    }

    @Transactional
    public void saveQuestionSave(Survey survey,List<QuestionRequestDto> questions, MultipartFile[] files, int i) throws IOException {
        String baseDir = applicationYmlRead.getBase_dir();
        List<Question> questionList = new ArrayList<>();
        for (QuestionRequestDto s : questions) {
            s.setSurvey(survey);
            Question question = questionRepository.save(new Question(s));

            if (s.isExistFile()) { // .png, .jpg, .jpeg 만 가능
                filename = files[i].getOriginalFilename();
                assert filename != null;
                if (filename.split("\\.")[1].equalsIgnoreCase("png")) {
                    filepath = baseDir + "/" + question.getId() + ".png";//files[i].getOriginalFilename();
                    question.setFilename(question.getId() + ".png");
                    files[i++].transferTo(new File(filepath));
                    //questionRepository.save(question);
                }
                else if (filename.split("\\.")[1].equalsIgnoreCase("jpg") || (filename.split("\\.")[1].equalsIgnoreCase("jpeg"))) {
                    filepath = baseDir + "/" + question.getId() + ".jpg";//files[i].getOriginalFilename();
                    question.setFilename(question.getId() + ".jpg");
                    files[i++].transferTo(new File(filepath));
                    //questionRepository.save(question);
                }
                else if (filename.split("\\.")[1].equalsIgnoreCase("mp4")) {
                    filepath = baseDir + "/" + question.getId() + ".mp4";//files[i].getOriginalFilename();
                    question.setFilename(question.getId() + ".mp4");
                    files[i++].transferTo(new File(filepath));
                    //questionRepository.save(question);
                }
                questionList.add(question);
            }
            questionRepository.saveAll(questionList);

            if (s.getQuestionCategoryId() == 1 || s.getQuestionCategoryId() == 2 || s.getQuestionCategoryId() == 3) {
                // 질문 카테고리가 객관식, 체크박스, 드롭다운일 때
                List<QuestionChoice> list = new ArrayList<>();
                for (int j = 0; j < s.getChoiceTexts().length; j++) {
                    list.add(new QuestionChoice(survey, question, s.getChoiceTexts()[j]));
                }
                questionChoiceRepository.saveAll(list);
            }
        }
    }
}
