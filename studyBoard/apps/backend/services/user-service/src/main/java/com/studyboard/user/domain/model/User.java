package com.studyboard.user.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private String username;

    @Builder
    public User(String email, String password, String username) {
        this.email = email;
        this.password = passwordEncoder.encode(password);
        this.username = username;
        this.role = role != null ? role : Role.USER;
    }

    public void setPassword(String newPassword) {
        this.password = passwordEncoder.encode(newPassword);
    }

    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }
}