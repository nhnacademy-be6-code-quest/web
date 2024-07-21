package com.nhnacademy.codequestweb.response.shipping;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShippingPolicyGetResponseDtoTest {

    @Test
    void testConstructorAndGetters() {
        String description = "Standard Shipping";
        int shippingFee = 3000;
        int minPurchaseAmount = 50000;
        String shippingPolicyType = "STANDARD";

        ShippingPolicyGetResponseDto dto = new ShippingPolicyGetResponseDto(
                description, shippingFee, minPurchaseAmount, shippingPolicyType);

        assertEquals(description, dto.description());
        assertEquals(shippingFee, dto.shippingFee());
        assertEquals(minPurchaseAmount, dto.minPurchaseAmount());
        assertEquals(shippingPolicyType, dto.shippingPolicyType());
    }

    @Test
    void testBuilder() {
        ShippingPolicyGetResponseDto dto = ShippingPolicyGetResponseDto.builder()
                .description("Express Shipping")
                .shippingFee(5000)
                .minPurchaseAmount(100000)
                .shippingPolicyType("EXPRESS")
                .build();

        assertEquals("Express Shipping", dto.description());
        assertEquals(5000, dto.shippingFee());
        assertEquals(100000, dto.minPurchaseAmount());
        assertEquals("EXPRESS", dto.shippingPolicyType());
    }

    @Test
    void testEquality() {
        ShippingPolicyGetResponseDto dto1 = new ShippingPolicyGetResponseDto(
                "Standard Shipping", 3000, 50000, "STANDARD");
        ShippingPolicyGetResponseDto dto2 = new ShippingPolicyGetResponseDto(
                "Standard Shipping", 3000, 50000, "STANDARD");
        ShippingPolicyGetResponseDto dto3 = new ShippingPolicyGetResponseDto(
                "Express Shipping", 5000, 100000, "EXPRESS");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        ShippingPolicyGetResponseDto dto1 = new ShippingPolicyGetResponseDto(
                "Standard Shipping", 3000, 50000, "STANDARD");
        ShippingPolicyGetResponseDto dto2 = new ShippingPolicyGetResponseDto(
                "Standard Shipping", 3000, 50000, "STANDARD");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ShippingPolicyGetResponseDto dto = new ShippingPolicyGetResponseDto(
                "Standard Shipping", 3000, 50000, "STANDARD");

        String expected = "ShippingPolicyGetResponseDto[description=Standard Shipping, shippingFee=3000, minPurchaseAmount=50000, shippingPolicyType=STANDARD]";
        assertEquals(expected, dto.toString());
    }
}