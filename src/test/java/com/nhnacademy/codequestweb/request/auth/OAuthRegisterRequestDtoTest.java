package com.nhnacademy.codequestweb.request.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OAuthRegisterRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto("accessToken", "TestName", birthDate);
        assertEquals("accessToken", dto.getAccess());
        assertEquals("TestName", dto.getName());
        assertEquals(birthDate, dto.getBirth());
    }

    @Test
    void getAccess() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        dto.setAccess("accessToken");
        assertEquals("accessToken", dto.getAccess());
    }

    @Test
    void getName() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        dto.setName("TestName");
        assertEquals("TestName", dto.getName());
    }

    @Test
    void getBirth() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        dto.setBirth(birthDate);
        assertEquals(birthDate, dto.getBirth());
    }

    @Test
    void setAccess() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        dto.setAccess("accessToken1");
        assertEquals("accessToken1", dto.getAccess());
    }

    @Test
    void setName() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        dto.setName("TestName1");
        assertEquals("TestName1", dto.getName());
    }

    @Test
    void setBirth() {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        LocalDate birthDate = LocalDate.of(1990, 1, 2);
        dto.setBirth(birthDate);
        assertEquals(birthDate, dto.getBirth());
    }
}
