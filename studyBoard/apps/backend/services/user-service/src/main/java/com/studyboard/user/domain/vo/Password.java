package com.studyboard.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Password {
    @Column(name = "password", nullable = false)
    private String value;

    protected Password() {}

    public Password(String value) {
        validatePassword(value);
        this.value = value;
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}