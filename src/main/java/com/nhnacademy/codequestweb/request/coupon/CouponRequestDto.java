package com.nhnacademy.codequestweb.request.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CouponRequestDto(
        @NotNull(message = "쿠폰 타입은 비어있을수 없습니다.")
        long couponTypeId,
        long couponPolicyId,
        @NotNull
        List<Long> clientId,
        LocalDateTime expirationDate,
        @NotNull
        Status status) {

}
