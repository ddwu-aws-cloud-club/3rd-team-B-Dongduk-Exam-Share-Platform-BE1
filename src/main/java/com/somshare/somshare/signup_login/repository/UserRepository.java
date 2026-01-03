package com.somshare.somshare.signup_login.repository;

import com.somshare.somshare.signup_login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // 로그인용 (유저가 있는지 조회)
    Optional<User> findByEmail(String email);
    // 회원가입용 (이미 가입했는지 조회)
    boolean existsByEmail(String email);
}