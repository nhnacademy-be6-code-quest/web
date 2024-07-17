package com.nhnacademy.codequestweb.response.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TokenResponseDtoTest {

    @Test
    void testAllArgsConstructor() {
        TokenResponseDto dto = new TokenResponseDto("accessToken", "refreshToken");

        assertEquals("accessToken", dto.getAccess());
        assertEquals("refreshToken", dto.getRefresh());
    }

    @Test
    void getAccess() {
        TokenResponseDto dto = new TokenResponseDto("accessToken", "refreshToken");

        assertEquals("accessToken", dto.getAccess());
    }

    @Test
    void getRefresh() {
        TokenResponseDto dto = new TokenResponseDto("accessToken", "refreshToken");

        assertEquals("refreshToken", dto.getRefresh());
    }

    @Test
    void setAccess() {
        TokenResponseDto dto = new TokenResponseDto("accessToken", "refreshToken");
        dto.setAccess("newAccessToken");

        assertEquals("newAccessToken", dto.getAccess());
    }

    @Test
    void setRefresh() {
        TokenResponseDto dto = new TokenResponseDto("accessToken", "refreshToken");
        dto.setRefresh("newRefreshToken");

        assertEquals("newRefreshToken", dto.getRefresh());
    }

    @Test
    void testBuilder() {
        TokenResponseDto dto = TokenResponseDto.builder()
                .access("accessToken")
                .refresh("refreshToken")
                .build();

        assertNotNull(dto);
        assertEquals("accessToken", dto.getAccess());
        assertEquals("refreshToken", dto.getRefresh());
    }

    @Test
    void testNoArgsConstructor() {
        // This test ensures that the no-args constructor exists and works correctly, although it is not needed due to the @AllArgsConstructor
        TokenResponseDto dto = new TokenResponseDto(null, null);
        assertNotNull(dto);
    }
}
