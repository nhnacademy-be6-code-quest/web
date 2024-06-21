package com.nhnacademy.codequestweb.client.auth;

import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientLoginResponseDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth", url = "http://localhost:8001")
public interface AuthClient {
    @PostMapping("/login")
    ResponseEntity<ClientLoginResponseDto> login(@RequestBody ClientLoginRequestDto clientLoginRequestDto);
    @PostMapping("/logout")
    ResponseEntity<?> logout(@RequestHeader HttpHeaders headers);
    @PostMapping("/reissue")
    ResponseEntity<ClientLoginResponseDto> reissue(@RequestHeader(name = "refresh") String refresh);
    @PostMapping("/api/client")
    ResponseEntity<ClientRegisterResponseDto> register(@Valid @RequestBody
                                                       ClientRegisterRequestDto clientRegisterRequestDto);
}
