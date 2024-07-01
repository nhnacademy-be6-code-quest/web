package com.nhnacademy.codequestweb.client.auth;

import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

//@FeignClient(name = "auth", url = "http://10.220.222.13:8001")
@FeignClient(name = "auth", url = "http://localhost:8001")
public interface AuthClient {
    @PostMapping("/api/login")
    ResponseEntity<TokenResponseDto> login(@RequestBody ClientLoginRequestDto clientLoginRequestDto);
    @PostMapping("/api/logout")
    ResponseEntity<String> logout(@RequestHeader HttpHeaders headers);
    @PostMapping("/api/reissue")
    ResponseEntity<TokenResponseDto> reissue(@RequestHeader(name = "refresh") String refresh);
}
