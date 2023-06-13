package com.example.jwtrefresh.service;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwtrefresh.dto.AccountReqDto;
import com.example.jwtrefresh.dto.GlobalResDto;
import com.example.jwtrefresh.dto.LoginReqDto;
import com.example.jwtrefresh.dto.TokenDto;
import com.example.jwtrefresh.entity.Account;
import com.example.jwtrefresh.entity.RefreshToken;
import com.example.jwtrefresh.repository.AccountRepository;
import com.example.jwtrefresh.repository.RefreshTokenRepository;
import com.example.jwtrefresh.token.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    
    @Transactional
    public GlobalResDto signup(AccountReqDto accountReqDto) {
        // nickname 중복검사
        if(accountRepository.findByEmail(accountReqDto.getEmail()).isPresent()){
            throw new RuntimeException("Overlap Check");
        }
        
        // 패스워드 암호화
        accountReqDto.setEncodePwd(passwordEncoder.encode(accountReqDto.getPw()));
        Account account = new Account(accountReqDto);
        
        // 회원가입 성공
        accountRepository.save(account);
        return new GlobalResDto("Success signup", HttpStatus.OK.value());
    }

    @Transactional
    public GlobalResDto login(LoginReqDto loginReqDto, HttpServletResponse response) {
        
        // 아이디 검사
        Account account = accountRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(
                () -> new RuntimeException("Not found Account")
        );
        
        // 비밀번호 검사
        if(!passwordEncoder.matches(loginReqDto.getPassword(), account.getPw())) {
            throw new RuntimeException("Not matches Password");
        }
        
        // 아이디 정보로 Token생성
        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getEmail());
        
        // Refresh토큰 있는지 확인 
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(loginReqDto.getEmail());
        
        // 있다면 새토큰 발급후 업데이트
        // 없다면 새로 만들고 디비 저장
        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getEmail());
            refreshTokenRepository.save(newToken);
        }
        
        // response 헤더에 Access Token / Refresh Token 넣음
        setHeader(response, tokenDto);
        return new GlobalResDto("Success Login", HttpStatus.OK.value());

    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
