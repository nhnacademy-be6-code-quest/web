package com.nhnacademy.codequestweb.response.mypage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientDeliveryAddressResponseDtoTest {

    @Test
    void testAllArgsConstructor() {
        ClientDeliveryAddressResponseDto dto = new ClientDeliveryAddressResponseDto(
                1L, "123 Main St", "Apt 4B", "Home", 12345);

        assertEquals(1L, dto.getClientDeliveryAddressId());
        assertEquals("123 Main St", dto.getClientDeliveryAddress());
        assertEquals("Apt 4B", dto.getClientDeliveryAddressDetail());
        assertEquals("Home", dto.getClientDeliveryAddressNickname());
        assertEquals(12345, dto.getClientDeliveryZipCode());
    }

    @Test
    void testGetters() {
        ClientDeliveryAddressResponseDto dto = new ClientDeliveryAddressResponseDto(
                2L, "123 Main St", "Apt 4B", "Home", 12345);

        assertEquals(2L, dto.getClientDeliveryAddressId());
        assertEquals("123 Main St", dto.getClientDeliveryAddress());
        assertEquals("Apt 4B", dto.getClientDeliveryAddressDetail());
        assertEquals("Home", dto.getClientDeliveryAddressNickname());
        assertEquals(12345, dto.getClientDeliveryZipCode());
    }

    @Test
    void testSetters() {
        ClientDeliveryAddressResponseDto dto = new ClientDeliveryAddressResponseDto(
                1L, "123 Main St", "Apt 4B", "Home", 12345);

        dto.setClientDeliveryAddressId(2L);
        dto.setClientDeliveryAddress("456 Elm St");
        dto.setClientDeliveryAddressDetail("Suite 5A");
        dto.setClientDeliveryAddressNickname("Office");
        dto.setClientDeliveryZipCode(67890);

        assertEquals(2L, dto.getClientDeliveryAddressId());
        assertEquals("456 Elm St", dto.getClientDeliveryAddress());
        assertEquals("Suite 5A", dto.getClientDeliveryAddressDetail());
        assertEquals("Office", dto.getClientDeliveryAddressNickname());
        assertEquals(67890, dto.getClientDeliveryZipCode());
    }

    @Test
    void testToString() {
        ClientDeliveryAddressResponseDto dto = new ClientDeliveryAddressResponseDto(
                1L, "123 Main St", "Apt 4B", "Home", 12345);

        String expected = "ClientDeliveryAddressResponseDto(clientDeliveryAddressId=1, clientDeliveryAddress=123 Main St, clientDeliveryAddressDetail=Apt 4B, clientDeliveryAddressNickname=Home, clientDeliveryZipCode=12345)";
        assertEquals(expected, dto.toString());
    }
}
