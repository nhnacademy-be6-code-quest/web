package com.nhnacademy.codequestweb.request.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientLoginRequestDtoTest {

    @Test
    void getClientEmail() {
        ClientLoginRequestDto dto = new ClientLoginRequestDto("test@example.com", "password");
        assertEquals("test@example.com", dto.getClientEmail());
    }

    @Test
    void getClientPassword() {
        ClientLoginRequestDto dto = new ClientLoginRequestDto("test@example.com", "password");
        assertEquals("password", dto.getClientPassword());
    }

    @Test
    void setClientEmail() {
        ClientLoginRequestDto dto = new ClientLoginRequestDto();
        dto.setClientEmail("test@example.com");
        assertEquals("test@example.com", dto.getClientEmail());
    }

    @Test
    void setClientPassword() {
        ClientLoginRequestDto dto = new ClientLoginRequestDto();
        dto.setClientPassword("password");
        assertEquals("password", dto.getClientPassword());
    }
}