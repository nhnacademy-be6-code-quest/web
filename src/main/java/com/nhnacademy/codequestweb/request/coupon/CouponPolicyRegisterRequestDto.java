package com.nhnacademy.codequestweb.request.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;




public record CouponPolicyRegisterRequestDto(
        String couponPolicyDescription,
        DiscountType discountType,
        long discountValue,
        long minPurchaseAmount,
        long maxDiscountAmount,
        Long id,
        String typeName
) {
}

