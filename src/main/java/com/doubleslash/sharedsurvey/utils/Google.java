package com.doubleslash.sharedsurvey.utils;

import java.util.List;

public class Google {
    // name, description, 질문리스트, 질문Choices리스트

    private String name; // 설문조사 이름

    private String description; // 설문조사 설명

    private List<GoogleQuestion> googleQuestions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GoogleQuestion> getGoogleQuestions() {
        return googleQuestions;
    }

    public void setGoogleQuestions(List<GoogleQuestion> googleQuestions) {
        this.googleQuestions = googleQuestions;
    }

    @Override
    public String toString() {
        return "Google{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", googleQuestions=" + googleQuestions +
                '}';
    }
}

