package com.studyboard.user.application.service;

import com.studyboard.user.application.dto.request.UserCreateRequest;
import com.studyboard.user.application.dto.request.UserPasswordUpdateRequest;
import com.studyboard.user.application.dto.response.UserCreateResponse;
import com.studyboard.user.domain.model.User;
import com.studyboard.user.domain.repository.UserRepository;
import com.studyboard.user.domain.vo.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getRequestEmail())) {
            return UserCreateResponse.emailDuplication(request.getRequestEmail());
        }

        try {
            User user = User.builder()
                    .username(request.getRequestUserName())
                    .email(request.getRequestEmail())
                    .password(request.getRequestPassword())
                    .build();

            user.setPassword(passwordEncoder.encode(request.getRequestPassword()));
            User savedUser = userRepository.save(user);
            return UserCreateResponse.success(savedUser.getId());
        } catch (Exception e) {
            return UserCreateResponse.error(e.getMessage());
        }
    }

    @Transactional
    public void updatePassword(Long userId, UserPasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword().getValue())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        try {
            new Password(request.getNewPassword());

            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }catch (IllegalArgumentException e){
            throw new RuntimeException("새 비밀번호가 유효하지 않습니다 : " +  e.getMessage());
        }
    }
}
