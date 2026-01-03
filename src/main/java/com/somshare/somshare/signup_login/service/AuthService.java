package com.somshare.somshare.signup_login.service;

import com.somshare.somshare.signup_login.entity.Role;
import com.somshare.somshare.signup_login.entity.User;
import com.somshare.somshare.signup_login.dto.SignupRequestDto;
import com.somshare.somshare.signup_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();

        // 학교 도메인 검사 - 프론트에서 막아도 백엔드에서 한 번 더!
        if (!email.endsWith("@dongduk.ac.kr")) {
            throw new IllegalArgumentException("학교 이메일만 사용할 수 있습니다.");
        }

        // 중복 가입 확인
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .email(requestDto.getEmail())
                .passwordHash(encodedPassword)
                .nickname("솜솜이")
                .departmentId(UUID.randomUUID()) // 명세서에 있어서 구현은 해둠 -> 근데 프론트에 선택지가 없길래 임시값 넣음
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}