package com.nhnacademy.codequestweb.request.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;
import jakarta.validation.constraints.NotNull;

public record CouponPolicyRequestDto(
        Long productId,
        Long productCategoryId,
        @NotNull(message = "정책 설명은 필수 입력칸 입니다.")
        String PolicyDescription,
        @NotNull(message = "할인타입 설정은 필수 입력칸 입니다.")
        DiscountType discountType,
        @NotNull(message = "할인액/할인율은 필수 입력칸 입니다.")
        Long discountValue,
        long minPurchaseAmount,
        long maxDiscountAmount) {
}
