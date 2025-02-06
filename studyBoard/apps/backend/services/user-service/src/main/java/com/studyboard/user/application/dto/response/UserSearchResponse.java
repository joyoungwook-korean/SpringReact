package com.studyboard.user.application.dto.response;

import com.studyboard.user.domain.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserSearchResponse {
    private final Long id;
    private final String userName;
    private final String email;
    private final String userRole;
    private final boolean success;
    private final String message;
    private final List<String> errors;

    public static UserSearchResponse from(User user) {
        return UserSearchResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail().getValue())
                .success(true)
                .build();
    }

    public static UserSearchResponse notFoundId() {
        return UserSearchResponse.builder()
                .success(false)
                .message("등록되지 않은 Id입니다.")
                .build();
    }

    public static UserSearchResponse notFoundEmail(String email) {
        return UserSearchResponse.builder()
                .success(false)
                .message("등록되지 않은 email입니다. " + email)
                .build();
    }

    public static UserSearchResponse error(String errorMessage) {
        return UserSearchResponse.builder()
                .success(false)
                .message("예상 외의 에러가 발생했습니다.")
                .errors(List.of(errorMessage))
                .build();
    }

}
