package com.doubleslash.sharedsurvey.config.security.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN","관리자"),
    MEMBER("ROLE_MEMBER","일반사용자");

    private String key;
    private String value;
}
// Service에 사용하는 enum 객체