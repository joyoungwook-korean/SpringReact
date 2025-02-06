package com.studyboard.user.domain.model;

import com.studyboard.user.domain.vo.Email;
import com.studyboard.user.domain.vo.Password;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private String username;

    @Builder
    public User(String email, String password, String username) {
        this.email = new Email(email);
        this.password = new Password(password);
        this.username = username;
        this.role = role != null ? role : Role.USER;
    }

    public void setPassword(String newPassword) {
        this.password = new Password(newPassword);
    }

    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }
}
