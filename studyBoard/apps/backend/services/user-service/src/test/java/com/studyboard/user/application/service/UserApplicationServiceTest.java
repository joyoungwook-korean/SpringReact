package com.studyboard.user.application.service;

import com.studyboard.user.application.dto.request.UserCreateRequest;
import com.studyboard.user.application.dto.request.UserPasswordUpdateRequest;
import com.studyboard.user.application.dto.response.UserCreateResponse;
import com.studyboard.user.domain.model.User;
import com.studyboard.user.domain.repository.UserRepository;
import com.studyboard.user.domain.vo.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserApplicationService userApplicationService;

    private User user;

    @BeforeEach
    void setUp() {
        String encodedPassword = "encodedOldPassword";
        user = User.builder()
                .email("test@example.com")
                .password(encodedPassword)
                .username("testuser")
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    @DisplayName("비밀번호 업데이트 성공")
    void updatePassword_Success() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        Password newPassword = new Password("newPassword123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode(newPassword.getValue())).thenReturn("encodedNewPassword123");

        userApplicationService.updatePassword(1L, request);

        verify(passwordEncoder).matches("oldPassword", "encodedOldPassword");
        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword().getValue().equals("encodedNewPassword123")
        ));
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
    @DisplayName("유저를 찾을 수 없음")
    void updatePassword_UserNotFound() {
        UserPasswordUpdateRequest request = new UserPasswordUpdateRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userApplicationService.updatePassword(1L, request));

        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("유저 생성 성공")
    void createUser_Success() {
        UserCreateRequest request = new UserCreateRequest();
        request.setRequestEmail("test@example.com");
        request.setRequestUserName("testUser");
        request.setRequestPassword("testPassword123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode(request.getRequestPassword())).thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserCreateResponse response  = userApplicationService.createUser(request);

        assertTrue(response.isSuccess());
        assertEquals(user.getId(), response.getUserId());
    }

    @Test
    @DisplayName("유저 생성 실패 - 중복 email")
    void createUser_DuplicateEmail() {
        UserCreateRequest request = new UserCreateRequest();
        request.setRequestEmail("test@example.com");
        request.setRequestUserName("testUser");
        request.setRequestPassword("testPassword123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        UserCreateResponse response  = userApplicationService.createUser(request);

        assertFalse(response.isSuccess());
        assertEquals(response.getMessage(), "이미 사용중인 이메일입니다. " + request.getRequestEmail());
    }

    @Test
    @DisplayName("유저 생성 실패 - password 규칙이 맞지 않음")
    void createUser_InvalidPassword() {
        UserCreateRequest request = new UserCreateRequest();
        request.setRequestEmail("test123@naver.com");
        request.setRequestUserName("testUser");
        request.setRequestPassword("1234");

        when(userRepository.existsByEmail("test123@naver.com")).thenReturn(false);

        UserCreateResponse response = userApplicationService.createUser(request);

        assertFalse(response.isSuccess());
        assertEquals("회원가입에 실패 했습니다.", response.getMessage());
        assertEquals("비밀번호는 8자 이상이어야 합니다.", response.getErrors().get(0));
    }

    @Test
    @DisplayName("유저 생성 실패 - DB 에러")
    void createUser_SaveFailed() {
        UserCreateRequest request = new UserCreateRequest();
        request.setRequestEmail("test@test.com");
        request.setRequestPassword("password123");
        request.setRequestUserName("testUser");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(request.getRequestPassword())).thenReturn("encodedPassword123");
        when(userRepository.save(any())).thenThrow(new RuntimeException("DB 에러"));

        UserCreateResponse response = userApplicationService.createUser(request);

        assertFalse(response.isSuccess());
        assertEquals("회원가입에 실패 했습니다.", response.getMessage());
        assertEquals("DB 에러" ,response.getErrors().get(0));
    }

    @Test
    @DisplayName("유저 생성 실패 - Email 형식 오류")
    void createUser_InvalidEmail() {
        UserCreateRequest request = new UserCreateRequest();
        request.setRequestEmail("test");
        request.setRequestPassword("password123");
        request.setRequestUserName("testUser");

        when(userRepository.existsByEmail(any())).thenReturn(false);

        UserCreateResponse response = userApplicationService.createUser(request);

        assertFalse(response.isSuccess());
        assertEquals("회원가입에 실패 했습니다.", response.getMessage());
        assertEquals("유효하지 않은 이메일 포맷입니다.", response.getErrors().get(0));
    }
}
