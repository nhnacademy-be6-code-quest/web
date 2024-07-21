package com.nhnacademy.codequestweb.request.shipping;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminShippingPolicyPutRequestDtoTest {

    @Test
    void testConstructorAndGetters() {
        String description = "Standard Shipping";
        int shippingFee = 3000;
        int minPurchaseAmount = 50000;
        String shippingPolicyType = "STANDARD";

        AdminShippingPolicyPutRequestDto dto = new AdminShippingPolicyPutRequestDto(
                description, shippingFee, minPurchaseAmount, shippingPolicyType);

        assertEquals(description, dto.description());
        assertEquals(shippingFee, dto.shippingFee());
        assertEquals(minPurchaseAmount, dto.minPurchaseAmount());
        assertEquals(shippingPolicyType, dto.shippingPolicyType());
    }

    @Test
    void testBuilder() {
        AdminShippingPolicyPutRequestDto dto = AdminShippingPolicyPutRequestDto.builder()
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
        AdminShippingPolicyPutRequestDto dto1 = new AdminShippingPolicyPutRequestDto(
                "Standard Shipping", 3000, 50000, "STANDARD");
        AdminShippingPolicyPutRequestDto dto2 = new AdminShippingPolicyPutRequestDto(
                "Standard Shipping", 3000, 50000, "STANDARD");
        AdminShippingPolicyPutRequestDto dto3 = new AdminShippingPolicyPutRequestDto(
                "Express Shipping", 5000, 100000, "EXPRESS");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        AdminShippingPolicyPutRequestDto dto1 = new AdminShippingPolicyPutRequestDto(
                "Standard Shipping", 3000, 50000, "STANDARD");
        AdminShippingPolicyPutRequestDto dto2 = new AdminShippingPolicyPutRequestDto(
                "Standard Shipping", 3000, 50000, "STANDARD");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AdminShippingPolicyPutRequestDto dto = new AdminShippingPolicyPutRequestDto(
                "Standard Shipping", 3000, 50000, "STANDARD");

        String expected = "AdminShippingPolicyPutRequestDto[description=Standard Shipping, shippingFee=3000, minPurchaseAmount=50000, shippingPolicyType=STANDARD]";
        assertEquals(expected, dto.toString());
    }
}