package com.doubleslash.sharedsurvey.domain.entity;

import com.doubleslash.sharedsurvey.domain.dto.member.MemberRequestDto;
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
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false)
    private boolean gender; // 여자 - true

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int point;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Transient
    private List<String> roles = new ArrayList<>();

    //    @Enumerated(EnumType.STRING)
//    private Role role;

    public Member(MemberRequestDto requestDto){
        this.memberId = requestDto.getMemberId();
        this.age = requestDto.getAge();
        this.gender = requestDto.isGender();
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.point = 30;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
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
