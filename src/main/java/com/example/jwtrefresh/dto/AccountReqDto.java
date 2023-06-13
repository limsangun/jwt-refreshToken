package com.example.jwtrefresh.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountReqDto {

    @NotBlank
    private String email;
    @NotBlank
    private String pw;

    private String pwck;

    public AccountReqDto(String email, String pw, String pwck) {
        this.email = email;
        this.pw = pw;
        this.pwck = pwck;
    }

    public void setEncodePwd(String encodePwd) {
        this.pw = encodePwd;
    }
}