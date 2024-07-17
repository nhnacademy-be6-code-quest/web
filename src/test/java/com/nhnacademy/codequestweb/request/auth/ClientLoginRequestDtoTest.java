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

    @Test
    void testEquals() {
        ClientLoginRequestDto dto1 = new ClientLoginRequestDto("test@example.com", "password");
        ClientLoginRequestDto dto2 = new ClientLoginRequestDto("test@example.com", "password");
        ClientLoginRequestDto dto3 = new ClientLoginRequestDto("other@example.com", "password");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void canEqual() {
        ClientLoginRequestDto dto1 = new ClientLoginRequestDto("test@example.com", "password");
        ClientLoginRequestDto dto2 = new ClientLoginRequestDto("test@example.com", "password");
        assertTrue(dto1.canEqual(dto2));
    }

    @Test
    void testHashCode() {
        ClientLoginRequestDto dto1 = new ClientLoginRequestDto("test@example.com", "password");
        ClientLoginRequestDto dto2 = new ClientLoginRequestDto("test@example.com", "password");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ClientLoginRequestDto dto = new ClientLoginRequestDto("test@example.com", "password");
        String expected = "ClientLoginRequestDto(clientEmail=test@example.com, clientPassword=password)";
        assertEquals(expected, dto.toString());
    }
}