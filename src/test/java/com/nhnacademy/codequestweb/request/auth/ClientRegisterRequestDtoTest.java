package com.nhnacademy.codequestweb.request.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientRegisterRequestDtoTest {

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

    @Test
    void testEquals() {
        ClientRegisterRequestDto dto1 = new ClientRegisterRequestDto();
        dto1.setClientEmail("test@example.com");
        dto1.setClientPassword("Password123!");
        dto1.setClientName("TestName");
        dto1.setClientBirth(LocalDate.of(1990, 1, 1));
        dto1.setClientPhoneNumber("01012345678");

        ClientRegisterRequestDto dto2 = new ClientRegisterRequestDto();
        dto2.setClientEmail("test@example.com");
        dto2.setClientPassword("Password123!");
        dto2.setClientName("TestName");
        dto2.setClientBirth(LocalDate.of(1990, 1, 1));
        dto2.setClientPhoneNumber("01012345678");

        assertEquals(dto1, dto2);
    }

    @Test
    void canEqual() {
        ClientRegisterRequestDto dto1 = new ClientRegisterRequestDto();
        ClientRegisterRequestDto dto2 = new ClientRegisterRequestDto();
        assertTrue(dto1.canEqual(dto2));
    }

    @Test
    void testHashCode() {
        ClientRegisterRequestDto dto1 = new ClientRegisterRequestDto();
        dto1.setClientEmail("test@example.com");
        dto1.setClientPassword("Password123!");
        dto1.setClientName("TestName");
        dto1.setClientBirth(LocalDate.of(1990, 1, 1));
        dto1.setClientPhoneNumber("01012345678");

        ClientRegisterRequestDto dto2 = new ClientRegisterRequestDto();
        dto2.setClientEmail("test@example.com");
        dto2.setClientPassword("Password123!");
        dto2.setClientName("TestName");
        dto2.setClientBirth(LocalDate.of(1990, 1, 1));
        dto2.setClientPhoneNumber("01012345678");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ClientRegisterRequestDto dto = new ClientRegisterRequestDto();
        dto.setClientEmail("test@example.com");
        dto.setClientPassword("Password123!");
        dto.setClientName("TestName");
        dto.setClientBirth(LocalDate.of(1990, 1, 1));
        dto.setClientPhoneNumber("01012345678");

        String expected = "ClientRegisterRequestDto(clientEmail=test@example.com, clientPassword=Password123!, clientName=TestName, clientBirth=1990-01-01, clientPhoneNumber=01012345678)";
        assertEquals(expected, dto.toString());
    }
}
