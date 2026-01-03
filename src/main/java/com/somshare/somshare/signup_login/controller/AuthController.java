package com.somshare.somshare.signup_login.controller;

import com.somshare.somshare.signup_login.dto.*;
import com.somshare.somshare.signup_login.dto.EmailRequestDto;
import com.somshare.somshare.signup_login.dto.SignupRequestDto;
import com.somshare.somshare.signup_login.dto.VerificationCheckDto;
import com.somshare.somshare.signup_login.service.AuthService;
import com.somshare.somshare.signup_login.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    // 인증 메일 발송 API
    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestBody EmailRequestDto request) {
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("인증 메일이 발송되었습니다.");
    }

    // 인증 코드 확인 API
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationCheckDto request) {
        boolean isVerified = emailService.verifyCode(request.getEmail(), request.getCode());

        if (isVerified) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("인증 코드가 일치하지 않습니다.");
        }
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입 완료");
    }
}