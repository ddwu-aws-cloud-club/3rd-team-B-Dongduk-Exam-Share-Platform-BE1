package com.somshare.somshare.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, Integer points, Boolean isVerified) {
        this.email = email;
        this.password = password;
        this.points = points != null ? points : 0;
        this.isVerified = isVerified != null ? isVerified : false;
    }

    public void verify() {
        this.isVerified = true;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void deductPoints(int points) {
        if (this.points < points) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }
        this.points -= points;
    }
}
