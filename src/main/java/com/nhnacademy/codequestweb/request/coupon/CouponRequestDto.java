package com.nhnacademy.codequestweb.request.coupon;

import com.nhnacademy.codequestweb.domain.Status;

import java.time.LocalDateTime;

public record CouponRequestDto(long couponTypeId, long couponPolicyId, long clientId, LocalDateTime issuedDate, LocalDateTime expirationDate, Status status) {
}
