package com.nhnacademy.codequestweb.request.mypage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientRegisterAddressRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto(
                "123 Main St", "Apt 4B", "Home", 12345);
        assertEquals("123 Main St", dto.getClientDeliveryAddress());
        assertEquals("Apt 4B", dto.getClientDeliveryAddressDetail());
        assertEquals("Home", dto.getClientDeliveryAddressNickname());
        assertEquals(12345, dto.getClientDeliveryZipCode());
    }

    @Test
    void testBuilder() {
        ClientRegisterAddressRequestDto dto = ClientRegisterAddressRequestDto.builder()
                .clientDeliveryAddress("123 Main St")
                .clientDeliveryAddressDetail("Apt 4B")
                .clientDeliveryAddressNickname("Home")
                .clientDeliveryZipCode(12345)
                .build();

        assertEquals("123 Main St", dto.getClientDeliveryAddress());
        assertEquals("Apt 4B", dto.getClientDeliveryAddressDetail());
        assertEquals("Home", dto.getClientDeliveryAddressNickname());
        assertEquals(12345, dto.getClientDeliveryZipCode());
    }

    @Test
    void getClientDeliveryAddress() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryAddress("123 Main St");
        assertEquals("123 Main St", dto.getClientDeliveryAddress());
    }

    @Test
    void getClientDeliveryAddressDetail() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryAddressDetail("Apt 4B");
        assertEquals("Apt 4B", dto.getClientDeliveryAddressDetail());
    }

    @Test
    void getClientDeliveryAddressNickname() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryAddressNickname("Home");
        assertEquals("Home", dto.getClientDeliveryAddressNickname());
    }

    @Test
    void getClientDeliveryZipCode() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryZipCode(12345);
        assertEquals(12345, dto.getClientDeliveryZipCode());
    }

    @Test
    void setClientDeliveryAddress() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryAddress("123 Main St1");
        assertEquals("123 Main St1", dto.getClientDeliveryAddress());
    }

    @Test
    void setClientDeliveryAddressDetail() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryAddressDetail("Apt 4B1");
        assertEquals("Apt 4B1", dto.getClientDeliveryAddressDetail());
    }

    @Test
    void setClientDeliveryAddressNickname() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryAddressNickname("Home1");
        assertEquals("Home1", dto.getClientDeliveryAddressNickname());
    }

    @Test
    void setClientDeliveryZipCode() {
        ClientRegisterAddressRequestDto dto = new ClientRegisterAddressRequestDto();
        dto.setClientDeliveryZipCode(12346);
        assertEquals(12346, dto.getClientDeliveryZipCode());
    }
}
