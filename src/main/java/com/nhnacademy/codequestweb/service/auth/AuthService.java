package com.nhnacademy.codequestweb.service.auth;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthClient authClient;

    public ResponseEntity<ClientRegisterResponseDto> register(ClientRegisterRequestDto clientRegisterRequestDto) {
        return authClient.register(clientRegisterRequestDto);
    }
}
