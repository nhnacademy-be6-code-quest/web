package com.nhnacademy.codequestweb.response.auth.coupon;

import com.nhnacademy.codequestweb.domain.Status;

import java.time.LocalDateTime;

public record CouponResponseDto(
    long couponId,
    CouponTypeResponseDto couponType,
    CouponPolicyResponseDto couponPolicy,
    LocalDateTime issuedDate,
    LocalDateTime expirationDate,
    LocalDateTime usedDate,
    Status status) {
}
