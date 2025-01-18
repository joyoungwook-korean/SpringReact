package com.studyboard.user.application.service;

import com.studyboard.user.application.dto.UserPasswordUpdateRequest;
import com.studyboard.user.domain.model.User;
import com.studyboard.user.domain.repository.UserRepository;
import com.studyboard.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void updatePassword(Long userId, UserPasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 현재 비밀번호 확인 (암호화된 비밀번호 비교)
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 유효성 검증
        if (!userDomainService.isValidPassword(request.getNewPassword())) {
            throw new RuntimeException("새 비밀번호가 유효하지 않습니다.");
        }

        // 비밀번호 암호화 후 업데이트
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}