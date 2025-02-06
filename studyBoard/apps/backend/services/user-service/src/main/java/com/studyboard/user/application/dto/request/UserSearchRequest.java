package com.studyboard.user.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {
    private Long id;
    private String email;
}
