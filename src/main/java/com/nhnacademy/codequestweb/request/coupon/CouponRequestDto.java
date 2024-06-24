package com.nhnacademy.codequestweb.request.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

public record CouponRequestDto(
        @NotNull(message = "쿠폰 타입은 비어있을수 없습니다.")
        long couponTypeId,
        long couponPolicyId,
        @NotNull
        long clientId,
        LocalDateTime expirationDate,
        @NotNull
        Status status) {

}
