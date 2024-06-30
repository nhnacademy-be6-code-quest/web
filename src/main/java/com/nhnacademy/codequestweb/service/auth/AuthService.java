package com.nhnacademy.codequestweb.service.auth;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.auth.OAuthRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${payco.client.id}")
    private String clientId;
    @Value("${payco.redirect.uri}")
    private String redirectUri;

    private final UserClient userClient;
    private final AuthClient authClient;

    public ResponseEntity<ClientRegisterResponseDto> register(ClientRegisterRequestDto clientRegisterRequestDto) {
        return userClient.register(clientRegisterRequestDto);
    }

    public ResponseEntity<TokenResponseDto> login(ClientLoginRequestDto clientLoginRequestDto) {
        return authClient.login(clientLoginRequestDto);
    }

    public ResponseEntity<String> logout(HttpHeaders headers) {
        return authClient.logout(headers);
    }

    public ResponseEntity<TokenResponseDto> reissue(String refreshToken) {
        return authClient.reissue(refreshToken);
    }

    public String getPaycoLoginURL() {
        return "https://id.payco.com/oauth2.0/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&serviceProviderCode=FRIENDS" +
                "&userLocale=ko_KR";
    }

    public ResponseEntity<TokenResponseDto> paycoLoginCallback(String code) {
        return authClient.paycoLoginCallback(code);
    }

    public ResponseEntity<TokenResponseDto> oAuthRegister(OAuthRegisterRequestDto oAuthRegisterRequestDto) {
        return authClient.oAuthRegister(oAuthRegisterRequestDto);
    }
}
