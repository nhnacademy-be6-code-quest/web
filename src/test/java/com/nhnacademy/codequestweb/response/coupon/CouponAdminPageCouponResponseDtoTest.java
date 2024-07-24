package com.nhnacademy.codequestweb.response.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class CouponAdminPageCouponResponseDtoTest {
    @Test
    void testCouponAdminPageCouponResponseDto() {
        // Create instance of CouponAdminPageCouponResponseDto
        CouponAdminPageCouponResponseDto dto = new CouponAdminPageCouponResponseDto();
        // Set some values for testing
        dto.setClientId(1L);
        dto.setIssuedDate("2023-07-06");
        dto.setUsedDate("2023-07-07");
        dto.setExpirationDate("2023-12-31");
        dto.setStatus("Available");
        // Create instance of CouponPolicy
        CouponAdminPageCouponResponseDto.CouponPolicy policy = new CouponAdminPageCouponResponseDto.CouponPolicy();
        policy.setCouponPolicyDescription("Birthday Discount");
        policy.setDiscountType("AMOUNTDISCOUNT");
        policy.setDiscountValue(1000L);
        policy.setMinPurchaseAmount(5000L);
        policy.setMaxDiscountAmount(2000L);
        // Set CouponPolicy instance to the dto
        dto.setCouponPolicy(policy);
        // Set couponKind
        dto.setCouponKind("BIRTHDAY");
        // Test getter methods
        assertEquals(1L, dto.getClientId());
        assertEquals("2023-07-06", dto.getIssuedDate());
        assertEquals("2023-07-07", dto.getUsedDate());
        assertEquals("2023-12-31", dto.getExpirationDate());
        assertEquals("Available", dto.getStatus());
        CouponAdminPageCouponResponseDto.CouponPolicy retrievedPolicy = dto.getCouponPolicy();
        assertNotNull(retrievedPolicy);
        assertEquals("Birthday Discount", retrievedPolicy.getCouponPolicyDescription());
        assertEquals("AMOUNTDISCOUNT", retrievedPolicy.getDiscountType());
        assertEquals(1000L, retrievedPolicy.getDiscountValue());
        assertEquals(5000L, retrievedPolicy.getMinPurchaseAmount());
        assertEquals(2000L, retrievedPolicy.getMaxDiscountAmount());
        assertEquals("BIRTHDAY", dto.getCouponKind());
    }
}