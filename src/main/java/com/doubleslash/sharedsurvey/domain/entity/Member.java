package com.doubleslash.sharedsurvey.domain.entity;

import com.doubleslash.sharedsurvey.domain.dto.MemberRequestDto;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Member {
// id, memberId, gender, age, name, password, point
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    private boolean gender; // 여자 - true

    private int age;

    private String name;

    private String password;

    private int point;

    public Member(MemberRequestDto requestDto){
        this.memberId = requestDto.getMemberId();
        this.age = requestDto.getAge();
        this.gender = requestDto.isGender();
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.point = 30;
    }

    public Member(){}

}
