package com.nhnacademy.codequestweb.response.coupon;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nhnacademy.codequestweb.domain.DiscountType;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CouponPolicyListResponseDto {

    private long couponPolicyId;
    private String couponPolicyDescription;
    private DiscountType discountType;
    private long discountValue;
    private long minPurchaseAmount;
    private long maxDiscountAmount;
}