package com.doubleslash.sharedsurvey.domain.dto.member;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberRequestDto {
    private String memberId;

    private boolean gender;

    private int age;

    private String name;

    private String password;

}
