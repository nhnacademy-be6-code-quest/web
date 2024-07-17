package com.nhnacademy.codequestweb.response.mypage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientPhoneNumberResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        ClientPhoneNumberResponseDto dto = new ClientPhoneNumberResponseDto();
        assertNotNull(dto);
    }

    @Test
    void testGetters() {
        ClientPhoneNumberResponseDto dto = new ClientPhoneNumberResponseDto();
        dto.setClientNumberId(1L);
        dto.setClientPhoneNumber("01012345678");

        assertEquals(1L, dto.getClientNumberId());
        assertEquals("01012345678", dto.getClientPhoneNumber());
    }

    @Test
    void testSetters() {
        ClientPhoneNumberResponseDto dto = new ClientPhoneNumberResponseDto();
        dto.setClientNumberId(2L);
        dto.setClientPhoneNumber("01087654321");

        assertEquals(2L, dto.getClientNumberId());
        assertEquals("01087654321", dto.getClientPhoneNumber());
    }

    @Test
    void testToString() {
        ClientPhoneNumberResponseDto dto = new ClientPhoneNumberResponseDto();
        dto.setClientNumberId(1L);
        dto.setClientPhoneNumber("01012345678");

        String expected = "ClientPhoneNumberResponseDto(clientNumberId=1, clientPhoneNumber=01012345678)";
        assertEquals(expected, dto.toString());
    }
}
