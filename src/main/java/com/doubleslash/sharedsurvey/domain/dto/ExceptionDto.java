package com.doubleslash.sharedsurvey.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDto {

    private boolean success = false;
    private String message;

    public ExceptionDto(String message){
        this.message = message;
    }
}
