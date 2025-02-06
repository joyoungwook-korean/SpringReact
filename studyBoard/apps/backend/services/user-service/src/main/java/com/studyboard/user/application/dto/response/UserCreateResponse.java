package com.studyboard.user.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserCreateResponse {
    private final boolean success;
    private final Long userId;
    private final String message;
    private final List<String> errors;


    @Builder
    public UserCreateResponse(boolean success, Long userId, String message, List<String> errors){
        this.success = success;
        this.userId = userId;
        this.message = message;
        this.errors = errors != null ? errors : new ArrayList<>();
    }

    public static UserCreateResponse emailDuplication(String email) {
        return UserCreateResponse.builder()
                .success(false)
                .message("이미 사용중인 이메일입니다. " + email)
                .build();
    }

    public static UserCreateResponse success(Long userId) {
        return UserCreateResponse.builder()
                .success(true)
                .userId(userId)
                .message("회원가입이 정상적으로 처리 되었습니다.")
                .build();
    }

    public static UserCreateResponse error(String errorMessage) {
        return UserCreateResponse.builder()
                .success(false)
                .message("회원가입에 실패 했습니다.")
                .errors(List.of(errorMessage))
                .build();
    }
}
