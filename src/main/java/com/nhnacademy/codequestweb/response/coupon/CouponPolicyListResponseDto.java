package com.nhnacademy.codequestweb.response.coupon;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CouponPolicyListResponseDto {

    long couponPolicyId;
    String couponPolicyDescription;
    String discountType;
    long discountValue;

}

