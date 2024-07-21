package com.nhnacademy.codequestweb.service.client;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.client.message.MessageClient;
import com.nhnacademy.codequestweb.request.client.ClientChangePasswordRequestDto;
import com.nhnacademy.codequestweb.request.client.ClientRecoveryRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    @Mock
    private UserClient userClient;

    @Mock
    private MessageClient messageClient;

    @InjectMocks
    private ClientServiceImp clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangePassword() {
        ClientChangePasswordRequestDto requestDto = new ClientChangePasswordRequestDto();
        when(userClient.changePasswordClient(requestDto)).thenReturn(ResponseEntity.ok().build());

        clientService.changePassword(requestDto);

        verify(userClient, times(1)).changePasswordClient(requestDto);
    }

    @Test
    void testSendChangePassword() {
        String email = "test@example.com";
        String expectedResponse = "Password change email sent";
        when(messageClient.sendChangePassword(email)).thenReturn(ResponseEntity.ok(expectedResponse));

        String result = clientService.sendChangePassword(email);

        assertEquals(expectedResponse, result);
        verify(messageClient, times(1)).sendChangePassword(email);
    }

    @Test
    void testSendRecoveryAccount() {
        String email = "test@example.com";
        String expectedResponse = "Recovery email sent";
        when(messageClient.sendRecoverAccount(email)).thenReturn(ResponseEntity.ok(expectedResponse));

        String result = clientService.sendRecoveryAccount(email);

        assertEquals(expectedResponse, result);
        verify(messageClient, times(1)).sendRecoverAccount(email);
    }

    @Test
    void testRecoverAccount() {
        ClientRecoveryRequestDto requestDto = new ClientRecoveryRequestDto();
        String expectedResponse = "Account recovered";
        when(userClient.recoveryClient(requestDto)).thenReturn(ResponseEntity.ok(expectedResponse));

        String result = clientService.recoverAccount(requestDto);

        assertEquals(expectedResponse, result);
        verify(userClient, times(1)).recoveryClient(requestDto);
    }
}