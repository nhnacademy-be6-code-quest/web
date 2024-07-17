package com.nhnacademy.codequestweb.request.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientChangePasswordRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto("test@example.com", "token123", "NewPassword1!");
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("token123", dto.getToken());
        assertEquals("NewPassword1!", dto.getNewPassword());
    }

    @Test
    void getEmail() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto();
        dto.setEmail("test@example.com");
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void getToken() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto();
        dto.setToken("token123");
        assertEquals("token123", dto.getToken());
    }

    @Test
    void getNewPassword() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto();
        dto.setNewPassword("NewPassword1!");
        assertEquals("NewPassword1!", dto.getNewPassword());
    }

    @Test
    void setEmail() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto();
        dto.setEmail("test1@example.com");
        assertEquals("test1@example.com", dto.getEmail());
    }

    @Test
    void setToken() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto();
        dto.setToken("token1231");
        assertEquals("token1231", dto.getToken());
    }

    @Test
    void setNewPassword() {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto();
        dto.setNewPassword("NewPassword1!");
        assertEquals("NewPassword1!", dto.getNewPassword());
    }
}
