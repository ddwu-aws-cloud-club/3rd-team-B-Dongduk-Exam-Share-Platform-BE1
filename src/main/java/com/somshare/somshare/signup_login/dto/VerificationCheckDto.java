package com.somshare.somshare.signup_login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationCheckDto {
    private String email;
    private String code;
}
