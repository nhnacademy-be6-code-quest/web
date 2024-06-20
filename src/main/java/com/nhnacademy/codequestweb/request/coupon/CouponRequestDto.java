package com.nhnacademy.codequestweb.request.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponPolicyResponseDto;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponTypeResponseDto;

import java.time.LocalDateTime;

public record CouponRequestDto(CouponTypeResponseDto couponTypeId, CouponPolicyResponseDto couponPolicyId, long clientId, LocalDateTime issuedDate, LocalDateTime expirationDate, Status status) {
}
