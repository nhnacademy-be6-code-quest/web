package com.nhnacademy.codequestweb.request.mypage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientUpdatePrivacyRequestDtoTest {
    private ClientUpdatePrivacyRequestDto dto;

    @BeforeEach
    void setUp() {
        dto = new ClientUpdatePrivacyRequestDto();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        ClientUpdatePrivacyRequestDto dtoWithArgs = new ClientUpdatePrivacyRequestDto("John Doe", birthDate);
        assertNotNull(dtoWithArgs);
        assertEquals("John Doe", dtoWithArgs.getClientName());
        assertEquals(birthDate, dtoWithArgs.getClientBirth());
    }

    @Test
    void testGetClientName() {
        dto.setClientName("John Doe");
        assertEquals("John Doe", dto.getClientName());
    }

    @Test
    void testSetClientName() {
        dto.setClientName("John Doe1");
        assertEquals("John Doe1", dto.getClientName());
    }

    @Test
    void testGetClientBirth() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        dto.setClientBirth(birthDate);
        assertEquals(birthDate, dto.getClientBirth());
    }

    @Test
    void testSetClientBirth() {
        LocalDate birthDate = LocalDate.of(1990, 1, 2);
        dto.setClientBirth(birthDate);
        assertEquals(birthDate, dto.getClientBirth());
    }
}
