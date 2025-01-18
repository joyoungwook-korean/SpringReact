package com.studyboard.user.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    // 도메인 로직을 포함하는 메서드들
    // 예: 비밀번호 유효성 검증, 이메일 형식 검증 등

    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}