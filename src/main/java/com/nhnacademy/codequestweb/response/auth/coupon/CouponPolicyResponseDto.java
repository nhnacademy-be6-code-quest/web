package com.nhnacademy.codequestweb.response.auth.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;

public record CouponPolicyResponseDto(long couponPolicyId, long productId, long productCategoryId, String couponPolicyDescription, DiscountType discountType, long minPurchaseAmount, long maxDiscountAmount) {
}
