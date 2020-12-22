package com.doubleslash.sharedsurvey.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDto {
    private String memberId;

    private boolean gender;

    private int age;

    private String name;

    private String password;
}
