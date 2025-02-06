package com.studyboard.user.application.service;

import com.studyboard.user.application.dto.request.UserPasswordUpdateRequest;
import com.studyboard.user.domain.model.User;
import com.studyboard.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserApplicationServiceIntegrationTest {
    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        String encodedPassword = passwordEncoder.encode("oldPassword");

        user = User.builder()
                .email("test@example.com")
                .password(encodedPassword) // 인코딩된 비밀번호로 저장
                .username("testuser")
                .build();

        user = userRepository.save(user);
        // 저장된 비밀번호 확인
        System.out.println("Stored encoded password: " + user.getPassword());
        System.out.println("Password match test: " +
                passwordEncoder.matches("oldPassword", user.getPassword().getValue()));
    }

    @Test
    void updatePassword_Success() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        userApplicationService.updatePassword(user.getId(), request);

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches(request.getNewPassword(),
                updatedUser.getPassword().getValue()));
    }

    @Test
    void updatePassword_InvalidCurrentPassword() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword123");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userApplicationService.updatePassword(user.getId(), request));

        assertEquals("현재 비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void updatePassword_InvalidNewPassword() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("short");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userApplicationService.updatePassword(user.getId(), request));

        assertEquals("비밀번호는 8자 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    void updatePassword_UserNotFound() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userApplicationService.updatePassword(999L, request));

        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }
}
