package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Question;
import com.doubleslash.sharedsurvey.repository.FileRepository;
import com.doubleslash.sharedsurvey.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {
    private final FileRepository fileRepository;
    private final QuestionRepository questionRepository;

    public boolean storeFile(MultipartFile[] files, List<Long> questionIds) throws IOException {

        String baseDir = "C:\\Users\\82103\\IdeaProjects\\sharedsurvey\\src\\main\\resources\\static\\uploadFiles";
        String filePath = "";

        System.out.println(questionIds.size() + "   " + files.length);
        if(questionIds.size() != 0) {
            for (int i = 0; i < questionIds.size(); i++) {
                filePath = baseDir + "\\" + files[i].getOriginalFilename();
                files[i].transferTo(new File(filePath));

                Question question = questionRepository.findById(questionIds.get(i)).get();
                question.setFilename(files[i].getOriginalFilename());

                questionRepository.save(question);
            }
            return true;
        }
        else return false;
    }
}
