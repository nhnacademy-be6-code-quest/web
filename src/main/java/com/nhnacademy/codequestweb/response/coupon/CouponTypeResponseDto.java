package com.nhnacademy.codequestweb.response.coupon;

import com.nhnacademy.codequestweb.domain.CouponKind;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponTypeResponseDto {
    long couponTypeId;
    CouponKind couponKind;
}