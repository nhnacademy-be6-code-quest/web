package com.nhnacademy.codequestweb.domain.entity;

import com.nhnacademy.codequestweb.domain.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CouponPolicy {
    private long couponPolicyId;
    private long productId;
    private long productCategoryId;
    private String couponPolicyDescription;
    private DiscountType discountType;
    private long diCountValue;
    private long minPurchaseAmount;
    private long maxDiscountAmount;
}
