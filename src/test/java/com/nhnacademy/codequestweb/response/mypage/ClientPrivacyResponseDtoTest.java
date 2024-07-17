package com.nhnacademy.codequestweb.response.mypage;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientPrivacyResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        ClientPrivacyResponseDto dto = new ClientPrivacyResponseDto();
        assertNotNull(dto);
    }

    @Test
    void testGetters() {
        ClientPrivacyResponseDto dto = new ClientPrivacyResponseDto();
        dto.setClientGrade("Gold");
        dto.setClientEmail("test@example.com");
        dto.setClientName("Test User");
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        dto.setClientBirth(birthDate);

        assertEquals("Gold", dto.getClientGrade());
        assertEquals("test@example.com", dto.getClientEmail());
        assertEquals("Test User", dto.getClientName());
        assertEquals(birthDate, dto.getClientBirth());
    }

    @Test
    void testSetters() {
        ClientPrivacyResponseDto dto = new ClientPrivacyResponseDto();
        dto.setClientGrade("Silver");
        dto.setClientEmail("test1@example.com");
        dto.setClientName("Test User1");
        LocalDate birthDate = LocalDate.of(1991, 2, 2);
        dto.setClientBirth(birthDate);

        assertEquals("Silver", dto.getClientGrade());
        assertEquals("test1@example.com", dto.getClientEmail());
        assertEquals("Test User1", dto.getClientName());
        assertEquals(birthDate, dto.getClientBirth());
    }

    @Test
    void testToString() {
        ClientPrivacyResponseDto dto = new ClientPrivacyResponseDto();
        dto.setClientGrade("Gold");
        dto.setClientEmail("test@example.com");
        dto.setClientName("Test User");
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        dto.setClientBirth(birthDate);

        String expected = "ClientPrivacyResponseDto(clientGrade=Gold, clientEmail=test@example.com, clientName=Test User, clientBirth=1990-01-01)";
        assertEquals(expected, dto.toString());
    }
}
