package com.nhnacademy.codequestweb.request.coupon;


import com.nhnacademy.codequestweb.domain.DiscountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponPolicyRegisterRequestDto(
    @NotNull
    String couponPolicyDescription,
    @NotNull
    DiscountType discountType,
    @NotNull
    @Min(message = "0 보다  커야됩니다.", value = 0)
    long discountValue,
    @NotNull
    long minPurchaseAmount,
    @NotNull
    @Min(message = "0 보다  커야됩니다.", value = 0)
    long maxDiscountAmount,
    Long id,
    String typeName
) {

}
