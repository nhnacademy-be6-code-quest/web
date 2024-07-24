package com.nhnacademy.codequestweb.response.coupon;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CouponPolicyListResponseDtoTest {
    @Test
    void testCouponPolicyListResponseDto() {
        CouponPolicyListResponseDto dto = new CouponPolicyListResponseDto();
        dto.setCouponPolicyId(1L);
        dto.setCouponPolicyDescription("Birthday Discount");
        dto.setDiscountType("AMOUNTDISCOUNT");
        dto.setDiscountValue(1000L);
        assertEquals(1L, dto.getCouponPolicyId());
        assertEquals("Birthday Discount", dto.getCouponPolicyDescription());
        assertEquals("AMOUNTDISCOUNT", dto.getDiscountType());
        assertEquals(1000L, dto.getDiscountValue());
    }
}