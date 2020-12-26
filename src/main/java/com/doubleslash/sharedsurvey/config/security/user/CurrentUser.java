package com.doubleslash.sharedsurvey.config.security.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;


@Target({ElementType.PARAMETER, ElementType.TYPE})
// 타겟은 파라미터에만 붙이겠다.
// 익명 사용자인 경우에는 null로, 익명 사용자가 아닌 경우에는 실제 account 객체로
@Retention(RetentionPolicy.RUNTIME)
// 런타임 까지 유지
@Documented
@AuthenticationPrincipal
// Principal 을 다이나믹 하게 꺼내기 위해 @CurrentUser 생성
public @interface CurrentUser { }
