package com.example.jwtrefresh.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReqDto {

	@NotBlank
    private String email;
    @NotBlank
    private String password;


    public LoginReqDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
