package com.nhnacademy.codequestweb.request.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;

public record CouponPolicyRequestDto(Long productId, Long productCategoryId, String couponPolicyDescription, DiscountType discountType, Long discountValue, long minPurchaseAmount, long maxDiscountAmount) {
}
