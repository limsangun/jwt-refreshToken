package com.example.jwtrefresh.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtrefresh.dto.AccountReqDto;
import com.example.jwtrefresh.dto.GlobalResDto;
import com.example.jwtrefresh.dto.LoginReqDto;
import com.example.jwtrefresh.entity.Account;
import com.example.jwtrefresh.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AccountController {

	private final AccountService accountService;
	
	@GetMapping("/test")
	public String test() {
		return "TEST";
	}
	
	// 회원가입
    @PostMapping("/api/account/join")
    public GlobalResDto join(@RequestBody Map<String, String> user) {
    	return accountService.signup(new AccountReqDto(user.get("email"), user.get("pw"), user.get("pwck")));
    }

    // 로그인
    @PostMapping("/api/account/login")
    public ResponseEntity<GlobalResDto> login(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
    	try {
            GlobalResDto result = accountService.login(loginReqDto, response);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GlobalResDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        }
    }
}
