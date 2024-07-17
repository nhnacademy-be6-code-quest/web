package com.nhnacademy.codequestweb.request.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientRecoveryRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        ClientRecoveryRequestDto dto = new ClientRecoveryRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        ClientRecoveryRequestDto dto = new ClientRecoveryRequestDto("test@example.com", "token123");
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("token123", dto.getToken());
    }

    @Test
    void getEmail() {
        ClientRecoveryRequestDto dto = new ClientRecoveryRequestDto();
        dto.setEmail("test@example.com");
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void getToken() {
        ClientRecoveryRequestDto dto = new ClientRecoveryRequestDto();
        dto.setToken("token123");
        assertEquals("token123", dto.getToken());
    }

    @Test
    void setEmail() {
        ClientRecoveryRequestDto dto = new ClientRecoveryRequestDto();
        dto.setEmail("test1@example.com");
        assertEquals("test1@example.com", dto.getEmail());
    }

    @Test
    void setToken() {
        ClientRecoveryRequestDto dto = new ClientRecoveryRequestDto();
        dto.setToken("token1231");
        assertEquals("token1231", dto.getToken());
    }
}
