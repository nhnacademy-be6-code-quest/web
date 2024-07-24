package com.nhnacademy.codequestweb.response.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class CouponOrderResponseDtoTest {
    @Test
    void testCouponOrderResponseDto() {
        CouponOrderResponseDto dto = new CouponOrderResponseDto();
        dto.setCouponId(1L);
        CouponOrderResponseDto.CouponPolicyDto policy = new CouponOrderResponseDto.CouponPolicyDto();
        policy.setCouponPolicyDescription("Birthday Discount");
        policy.setDiscountType("AMOUNTDISCOUNT");
        policy.setDiscountValue(1000L);
        policy.setMinPurchaseAmount(5000L);
        policy.setMaxDiscountAmount(2000L);
        dto.setCouponPolicyDto(policy);
        CouponOrderResponseDto.ProductCoupon productCoupon = new CouponOrderResponseDto.ProductCoupon();
        productCoupon.setProductId(123L);
        dto.setProductCoupon(productCoupon);
        CouponOrderResponseDto.CategoryCoupon categoryCoupon = new CouponOrderResponseDto.CategoryCoupon();
        categoryCoupon.setProductCategoryId(456L);
        dto.setCategoryCoupon(categoryCoupon);
        assertEquals(1L, dto.getCouponId());
        CouponOrderResponseDto.CouponPolicyDto retrievedPolicy = dto.getCouponPolicyDto();
        assertNotNull(retrievedPolicy);
        assertEquals("Birthday Discount", retrievedPolicy.getCouponPolicyDescription());
        assertEquals("AMOUNTDISCOUNT", retrievedPolicy.getDiscountType());
        assertEquals(1000L, retrievedPolicy.getDiscountValue());
        assertEquals(5000L, retrievedPolicy.getMinPurchaseAmount());
        assertEquals(2000L, retrievedPolicy.getMaxDiscountAmount());
        CouponOrderResponseDto.ProductCoupon retrievedProductCoupon = dto.getProductCoupon();
        assertNotNull(retrievedProductCoupon);
        assertEquals(123L, retrievedProductCoupon.getProductId());
        CouponOrderResponseDto.CategoryCoupon retrievedCategoryCoupon = dto.getCategoryCoupon();
        assertNotNull(retrievedCategoryCoupon);
        assertEquals(456L, retrievedCategoryCoupon.getProductCategoryId());
    }
}