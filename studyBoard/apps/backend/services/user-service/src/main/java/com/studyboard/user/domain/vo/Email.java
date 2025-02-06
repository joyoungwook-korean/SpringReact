package com.studyboard.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email {

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    protected Email(){}

    public Email(String value){
        validate(value);
        this.value = value;
    }

    private void validate(String value){
        if (value == null || !value.contains("@")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 포맷입니다.");
        }
    }

    public String getValue(){
        return value;
    }
}
