package com.studyboard.user.application.service;

import com.studyboard.user.application.dto.UserPasswordUpdateRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.studyboard.user.domain.model.User;
import com.studyboard.user.domain.repository.UserRepository;
import com.studyboard.user.domain.service.UserDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserApplicationService userApplicationService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .email("test@example.com")
                .password("oldPassword")
                .username("testuser")
                .build();
    }

    @Test
    @DisplayName("비밀번호 업데이트 성공")
    void updatePassword_Success() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDomainService.isValidPassword("newPassword123")).thenReturn(true);
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword123");

        userApplicationService.updatePassword(1L, request);

        assertEquals(passwordEncoder.encode("newPassword123"), "encodedNewPassword123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("비밀번호 업데이트 실패 - 현재 비밀번호 불일치")
    void updatePassword_InvalidCurrentPassword() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userApplicationService.updatePassword(1L, request));

        assertEquals("현재 비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatePassword_UserNotFound() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        when(userRepository.findByID(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userApplicationService.updatePassword(1L, request));

        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}