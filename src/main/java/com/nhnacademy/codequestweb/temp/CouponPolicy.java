package com.nhnacademy.codequestweb.temp;

import com.nhnacademy.codequestweb.domain.DiscountType;
import lombok.Getter;

@Getter
public class CouponPolicy {
    private long couponPolicyId;
    private Long productId;
    private Long productCategoryId;
    private String couponPolicyDescription;
    private DiscountType discountType;
    private long discountValue;
    private long minPurchaseAmount;
    private long maxDiscountAmount;
}
