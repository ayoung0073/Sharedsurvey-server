package com.doubleslash.sharedsurvey.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SuccessMessageDto {
    private boolean success;
    private String message;
}
