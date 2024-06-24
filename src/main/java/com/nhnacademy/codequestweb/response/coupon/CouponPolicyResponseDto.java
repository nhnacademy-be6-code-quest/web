package com.nhnacademy.codequestweb.response.coupon;

import com.nhnacademy.codequestweb.domain.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CouponPolicyResponseDto {
    private long couponPolicyId;
    private long productId;
    private long productCategoryId;
    private String policyDescription;
    private DiscountType discountType;
    private long discountValue;
    private long minPurchaseAmount;
    private long maxDiscountAmount;
    }