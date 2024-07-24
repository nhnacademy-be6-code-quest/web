package com.nhnacademy.codequestweb.response.coupon;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class CouponMyPageCouponResponseDtoTest {
    @Test
    void testCouponMyPageCouponResponseDto() {
        CouponMyPageCouponResponseDto dto = new CouponMyPageCouponResponseDto();
        dto.setIssuedDate("2023-07-06");
        dto.setUsedDate("2023-07-07");
        dto.setExpirationDate("2023-12-31");
        dto.setStatus("Used");
        CouponMyPageCouponResponseDto.CouponPolicy policy = new CouponMyPageCouponResponseDto.CouponPolicy();
        policy.setCouponPolicyDescription("Birthday Discount");
        policy.setDiscountType("AMOUNTDISCOUNT");
        policy.setDiscountValue(1000L);
        policy.setMinPurchaseAmount(5000L);
        policy.setMaxDiscountAmount(2000L);
        dto.setCouponPolicy(policy);
        dto.setCouponKind("BIRTHDAY");
        assertEquals("2023-07-06", dto.getIssuedDate());
        assertEquals("2023-07-07", dto.getUsedDate());
        assertEquals("2023-12-31", dto.getExpirationDate());
        assertEquals("Used", dto.getStatus());
        CouponMyPageCouponResponseDto.CouponPolicy retrievedPolicy = dto.getCouponPolicy();
        assertNotNull(retrievedPolicy);
        assertEquals("Birthday Discount", retrievedPolicy.getCouponPolicyDescription());
        assertEquals("AMOUNTDISCOUNT", retrievedPolicy.getDiscountType());
        assertEquals(1000L, retrievedPolicy.getDiscountValue());
        assertEquals(5000L, retrievedPolicy.getMinPurchaseAmount());
        assertEquals(2000L, retrievedPolicy.getMaxDiscountAmount());
        assertEquals("BIRTHDAY", dto.getCouponKind());
    }
}
