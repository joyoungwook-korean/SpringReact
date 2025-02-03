package com.studyboard.user.presentation.controller;

import com.studyboard.user.application.dto.request.UserPasswordUpdateRequest;
import com.studyboard.user.application.service.UserApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userApplicationService;

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordUpdateRequest request) {
        userApplicationService.updatePassword(id, request);
    }

//    @PostMapping("/create")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void createUser() {
//        userApplicationService.createUser();
//    }
}
