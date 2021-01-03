package com.doubleslash.sharedsurvey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableJpaAuditing
@SpringBootApplication
public class SharedsurveyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedsurveyApplication.class, args);
    }

}
