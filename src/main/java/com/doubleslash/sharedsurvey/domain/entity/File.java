package com.doubleslash.sharedsurvey.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class File {
    @Id
    private Long questionId;

    private String filename;

    private String contentType;

    private byte[] bytes;
}
