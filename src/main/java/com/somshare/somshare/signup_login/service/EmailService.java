package com.somshare.somshare.signup_login.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    // 임시 저장소 (실무에서는 Redis 사용 권장)
    // Key: 이메일, Value: 인증코드
    private final Map<String, String> verificationStore = new ConcurrentHashMap<>();

    // 1. 인증번호 전송
    public void sendVerificationCode(String email) {
        // 학번 추출 및 검증 로직은 필요하다면 여기서 수행
        // 예: if (!email.endsWith("@dongduk.ac.kr")) throw new Exception...

        String authCode = createCode();
        verificationStore.put(email, authCode); // 메모리에 저장

        try {
            sendMail(email, authCode);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패");
        }
    }

    // 2. 인증번호 검증
    public boolean verifyCode(String email, String code) {
        String storedCode = verificationStore.get(email);
        // 저장된 코드가 있고, 입력한 코드와 같다면 true
        if (storedCode != null && storedCode.equals(code)) {
            verificationStore.remove(email); // 인증 성공 시 삭제 (재사용 방지)
            return true;
        }
        return false;
    }

    private void sendMail(String toEmail, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject("[SomShare] 회원가입 인증번호");
        helper.setText("인증번호: <h1>" + authCode + "</h1>", true);
        mailSender.send(message);
    }

    private String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(10));
        }
        return key.toString();
    }
}