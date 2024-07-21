package com.nhnacademy.codequestweb.service.auth;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.auth.OAuthRegisterRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private UserClient userClient;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(authService, "clientId", "test-client-id");
        ReflectionTestUtils.setField(authService, "redirectUri", "http://test-redirect.com");
        ReflectionTestUtils.setField(authService, "recoverRedirectUri", "http://test-recover-redirect.com");
    }

    @Test
    void testRegister() {
        ClientRegisterRequestDto requestDto = new ClientRegisterRequestDto();
        ResponseEntity<ClientRegisterResponseDto> expectedResponse = ResponseEntity.ok(new ClientRegisterResponseDto("test@email.com", LocalDateTime.now()));
        when(userClient.register(requestDto)).thenReturn(expectedResponse);

        ResponseEntity<ClientRegisterResponseDto> result = authService.register(requestDto);

        assertEquals(expectedResponse, result);
        verify(userClient).register(requestDto);
    }

    @Test
    void testLogin() {
        ClientLoginRequestDto requestDto = new ClientLoginRequestDto();
        ResponseEntity<TokenResponseDto> expectedResponse = ResponseEntity.ok(TokenResponseDto.builder().build());
        when(authClient.login(requestDto)).thenReturn(expectedResponse);

        ResponseEntity<TokenResponseDto> result = authService.login(requestDto);

        assertEquals(expectedResponse, result);
        verify(authClient).login(requestDto);
    }

    @Test
    void testLogout() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Logged out");
        when(authClient.logout(headers)).thenReturn(expectedResponse);

        ResponseEntity<String> result = authService.logout(headers);

        assertEquals(expectedResponse, result);
        verify(authClient).logout(headers);
    }

    @Test
    void testGetPaycoLoginURL() {
        String result = authService.getPaycoLoginURL();
        assertTrue(result.contains("test-client-id"));
        assertTrue(result.contains("http://test-redirect.com"));
    }

    @Test
    void testGetPaycoRecoveryURL() {
        String result = authService.getPaycoRecoveryURL();
        assertTrue(result.contains("test-client-id"));
        assertTrue(result.contains("http://test-recover-redirect.com"));
    }

    @Test
    void testPaycoLoginCallback() {
        String code = "test-code";
        ResponseEntity<TokenResponseDto> expectedResponse = ResponseEntity.ok(TokenResponseDto.builder().build());
        when(authClient.paycoLoginCallback(code)).thenReturn(expectedResponse);

        ResponseEntity<TokenResponseDto> result = authService.paycoLoginCallback(code);

        assertEquals(expectedResponse, result);
        verify(authClient).paycoLoginCallback(code);
    }

    @Test
    void testOAuthRegister() {
        OAuthRegisterRequestDto requestDto = new OAuthRegisterRequestDto();
        ResponseEntity<TokenResponseDto> expectedResponse = ResponseEntity.ok(TokenResponseDto.builder().build());
        when(authClient.oAuthRegister(requestDto)).thenReturn(expectedResponse);

        ResponseEntity<TokenResponseDto> result = authService.oAuthRegister(requestDto);

        assertEquals(expectedResponse, result);
        verify(authClient).oAuthRegister(requestDto);
    }

    @Test
    void testRecover() {
        String code = "test-code";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Recovered");
        when(authClient.paycoRecoveryCallback(code)).thenReturn(expectedResponse);

        ResponseEntity<String> result = authService.recover(code);

        assertEquals(expectedResponse, result);
        verify(authClient).paycoRecoveryCallback(code);
    }

    @Test
    void testRecoverAccount() {
        String email = "test@example.com";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Account recovered");
        when(userClient.recoveryOauthClient(email)).thenReturn(expectedResponse);

        String result = authService.recoverAccount(email);

        assertEquals(expectedResponse.getBody(), result);
        verify(userClient).recoveryOauthClient(email);
    }
}