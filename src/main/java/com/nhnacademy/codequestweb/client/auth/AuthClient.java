package com.nhnacademy.codequestweb.client.auth;

import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.OAuthRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth", url = "http://localhost:8001")
public interface AuthClient {
    @PostMapping("/api/login")
    ResponseEntity<TokenResponseDto> login(@RequestBody ClientLoginRequestDto clientLoginRequestDto);
    @PostMapping("/api/logout")
    ResponseEntity<String> logout(@RequestHeader HttpHeaders headers);
    @PostMapping("/api/reissue")
    ResponseEntity<TokenResponseDto> reissue(@RequestHeader(name = "refresh") String refresh);
    @GetMapping("/api/payco/login/callback")
    ResponseEntity<TokenResponseDto> paycoLoginCallback(@RequestParam("code") String code);
    @PostMapping("/api/oauth")
    ResponseEntity<TokenResponseDto> oAuthRegister(@RequestBody OAuthRegisterRequestDto oAuthRegisterRequestDto);
    @GetMapping("/api/payco/recovery/callback")
    ResponseEntity<String> paycoRecoveryCallback(@RequestParam("code") String code);
}
