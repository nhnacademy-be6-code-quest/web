package com.nhnacademy.codequestweb.request.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientRegisterRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto(
                "test@example.com",
                "Password123!",
                "TestName",
                birthDate,
                "01012345678"
        );
        assertEquals("test@example.com", dto.getClientEmail());
        assertEquals("Password123!", dto.getClientPassword());
        assertEquals("TestName", dto.getClientName());
        assertEquals(birthDate, dto.getClientBirth());
        assertEquals("01012345678", dto.getClientPhoneNumber());
    }

    @Test
    void getClientEmail() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientEmail("test@example.com");
        assertEquals("test@example.com", dto.getClientEmail());
    }

    @Test
    void getClientPassword() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientPassword("Password123!");
        assertEquals("Password123!", dto.getClientPassword());
    }

    @Test
    void getClientName() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientName("TestName");
        assertEquals("TestName", dto.getClientName());
    }

    @Test
    void getClientBirth() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        dto.setClientBirth(birthDate);
        assertEquals(birthDate, dto.getClientBirth());
    }

    @Test
    void getClientPhoneNumber() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientPhoneNumber("01012345678");
        assertEquals("01012345678", dto.getClientPhoneNumber());
    }

    @Test
    void setClientEmail() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientEmail("test1@example.com");
        assertEquals("test1@example.com", dto.getClientEmail());
    }

    @Test
    void setClientPassword() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientPassword("Password123!1");
        assertEquals("Password123!1", dto.getClientPassword());
    }

    @Test
    void setClientName() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientName("TestName1");
        assertEquals("TestName1", dto.getClientName());
    }

    @Test
    void setClientBirth() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        LocalDate birthDate = LocalDate.of(1990, 1, 2);
        dto.setClientBirth(birthDate);
        assertEquals(birthDate, dto.getClientBirth());
    }

    @Test
    void setClientPhoneNumber() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientPhoneNumber("01012345679");
        assertEquals("01012345679", dto.getClientPhoneNumber());
    }
}
