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

}
