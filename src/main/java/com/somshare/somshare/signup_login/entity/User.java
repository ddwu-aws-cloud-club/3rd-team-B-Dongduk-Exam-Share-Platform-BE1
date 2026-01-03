package com.somshare.somshare.signup_login.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    // ID: UUID, Auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    // Email: 학교 이메일, Unique, Not Null
    @Column(nullable = false, unique = true)
    private String email;

    // PasswordHash: 비밀번호 해시 값, Not Null
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Nickname: 사용자 닉네임, Unique, Not Null
    @Column(nullable = false, unique = true)
    private String nickname;

    // DepartmentId: 소속 학과 ID (FK), Not Null
    // 현재 Department 엔티티 X ->  UUID 타입으로 직접 저장
    @Column(name = "department_id", nullable = false)
    private UUID departmentId;

    // Point: 현재 보유 포인트, Default 0
    @Column(nullable = false)
    @ColumnDefault("0") // DB 레벨 기본값
    private Integer point = 0; // 객체 레벨 기본값

    // IsEmailVerified: 이메일 인증 여부, Default False
    @Column(name = "is_email_verified", nullable = false)
    @ColumnDefault("false")
    private Boolean isEmailVerified = false;

    // CreatedAt: 생성 일시, Not Null
    @CreationTimestamp // INSERT 시 시간 자동 저장
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 역할 (Role)은 명세서엔 없지만, 시큐리티 로직을 위해 유지하는 것을 권장합니다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, String passwordHash, String nickname, UUID departmentId, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.departmentId = departmentId;
        this.role = role;       // 명세서는 없는데 필요하지 않나? 싶어서 넣어두엇습니다...
        // point, isEmailVerified, createdAt은 기본값/자동생성 사용
    }

    // 이메일 인증 성공 시 상태 변경 메서드
    public void verifyEmail() {
        this.isEmailVerified = true;
    }
}