package com.doubleslash.sharedsurvey.domain.entity;

import com.doubleslash.sharedsurvey.config.security.user.Role;
import com.doubleslash.sharedsurvey.domain.dto.member.MemberRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Member implements UserDetails {
// id, memberId, gender, age, name, password, point
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false)
    @JsonIgnore
    private boolean gender; // 여자 - true

    @Column(nullable = false)
    @JsonIgnore
    private int age;

    @Column(nullable = false)
    @JsonIgnore
    private String name;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @JsonIgnore
    private int point;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Transient
    @JsonIgnore
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;

    public Member(MemberRequestDto requestDto){
        this.memberId = requestDto.getMemberId();
        this.age = requestDto.getAge();
        this.gender = requestDto.isGender();
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.point = 30;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();

//        collectors.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return "ROLE_" + user.getRole(); // ROLE_ 꼭 넣어줘야 함.
//            }
//        });
//        같은 뜻
        collectors.add(()->{ return "ROLE_" + this.getRole();});
        return collectors;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return memberId;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public void usePoint(int point){
        this.point = this.point - point;
    }

    public void getPoint(int point){
        this.point = this.point + point;
    }
}
