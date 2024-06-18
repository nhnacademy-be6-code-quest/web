package com.nhnacademy.codequestweb.client.auth;

import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "register", url = "http://localhost:8001")
public interface AuthClient {
    @PostMapping("/api/client")
    ResponseEntity<ClientRegisterResponseDto> register(@Valid @RequestBody ClientRegisterRequestDto clientRegisterRequestDto);
}
