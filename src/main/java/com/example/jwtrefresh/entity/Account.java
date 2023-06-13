package com.example.jwtrefresh.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.example.jwtrefresh.dto.AccountReqDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @NotBlank
    private String email;
    @NotBlank
    private String pw;
    

    public Account(AccountReqDto accountReqDto) {
        this.email = accountReqDto.getEmail();
        this.pw = accountReqDto.getPw();
    }
}