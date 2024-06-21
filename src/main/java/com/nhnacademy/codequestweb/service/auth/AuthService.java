package com.nhnacademy.codequestweb.service.auth;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientLoginResponseDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserClient userClient;
    private final AuthClient authClient;

    public ResponseEntity<ClientRegisterResponseDto> register(ClientRegisterRequestDto clientRegisterRequestDto) {
        return userClient.register(clientRegisterRequestDto);
    }

    public ResponseEntity<ClientLoginResponseDto> login(ClientLoginRequestDto clientLoginRequestDto) {
        return authClient.login(clientLoginRequestDto);
    }

    public ResponseEntity<ClientLoginResponseDto> reissue(String refreshToken) {
        return authClient.reissue(refreshToken);
    }
}
