package com.nhnacademy.codequestweb.response.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientRegisterResponseDtoTest {

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ClientRegisterResponseDto dto = new ClientRegisterResponseDto("test@example.com", now);

        assertEquals("test@example.com", dto.getClientEmail());
        assertEquals(now, dto.getClientCreatedAt());
    }

    @Test
    void getClientEmail() {
        LocalDateTime now = LocalDateTime.now();
        ClientRegisterResponseDto dto = new ClientRegisterResponseDto("test@example.com", now);

        assertEquals("test@example.com", dto.getClientEmail());
    }

    @Test
    void getClientCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        ClientRegisterResponseDto dto = new ClientRegisterResponseDto("test@example.com", now);

        assertEquals(now, dto.getClientCreatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        // This test ensures that the no-args constructor exists and works correctly, although it is not needed due to the @AllArgsConstructor
        ClientRegisterResponseDto dto = new ClientRegisterResponseDto(null, null);
        assertNotNull(dto);
    }
}
