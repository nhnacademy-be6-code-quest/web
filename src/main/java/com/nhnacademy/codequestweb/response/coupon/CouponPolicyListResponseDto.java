package com.nhnacademy.codequestweb.response.coupon;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CouponPolicyListResponseDto {

    long couponPolicyId;
    String couponPolicyDescription;
    String discountType;
    long discountValue;

}

