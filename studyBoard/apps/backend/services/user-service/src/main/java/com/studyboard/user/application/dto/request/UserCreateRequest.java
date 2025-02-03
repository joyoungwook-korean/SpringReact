package com.studyboard.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    @NotBlank(message = "이메일은 필수 입니다.")
    private String requestEmail;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    private String requestPassword;

    @NotBlank(message = "유저이름은 필수 입니다.")
    private String requestUserName;
}
