package com.studyboard.user.application.service;

import com.studyboard.user.application.dto.request.UserCreateRequest;
import com.studyboard.user.application.dto.request.UserPasswordUpdateRequest;
import com.studyboard.user.application.dto.response.UserCreateResponse;
import com.studyboard.user.domain.model.User;
import com.studyboard.user.domain.repository.UserRepository;
import com.studyboard.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateResponse createUser(String email, String password, String username) {
        // 1. 입력값 검증을 먼저 수행합니다
        if (!userDomainService.isValidEmail(email)) {
            return UserCreateResponse.builder()
                    .success(false)
                    .message("이메일 형식이 맞지 않습니다.")
                    .build();
        }

        // 2. 이메일 중복 검사를 수행합니다
        if (userRepository.existsByEmail(email)) {
            return UserCreateResponse.builder()
                    .success(false)
                    .message("이미 사용중인 이메일입니다.")
                    .build();
        }

        try {
            // 3. 비밀번호 암호화 및 사용자 객체 생성
            String encodedPassword = passwordEncoder.encode(password);
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .password(encodedPassword)
                    .build();

            // 4. 데이터베이스에 저장
            User savedUser = userRepository.save(user);

            // 5. 성공 응답 생성
            return UserCreateResponse.builder()
                    .userId(savedUser.getId())
                    .success(true)
                    .message("회원가입이 성공적으로 완료되었습니다.")
                    .build();

        } catch (Exception e) {
            // 6. 예외 처리
            return UserCreateResponse.builder()
                    .success(false)
                    .message("회원가입 처리 중 오류가 발생했습니다.")
                    .errors(List.of(e.getMessage()))
                    .build();
        }
    }

    @Transactional
    public void updatePassword(Long userId, UserPasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        System.out.println(request.getCurrentPassword());
        System.out.println(user.getPassword());

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (!userDomainService.isValidPassword(request.getNewPassword())) {
            throw new RuntimeException("새 비밀번호가 유효하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
