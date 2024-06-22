package com.nhnacademy.codequestweb.response.coupon;

import com.nhnacademy.codequestweb.domain.CouponKind;

public record CouponTypeResponseDto(long couponTypeId, CouponKind couponKind) {
}
