package com.nhnacademy.codequestweb.request.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.codequestweb.domain.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public record CouponRequestDto(
        @NotNull(message = "쿠폰 타입은 비어있을수 없습니다.")
        long couponTypeId,
        long couponPolicyId,
        @NotNull
        List<Long> clientId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime expirationDate,
        @NotNull
        Status status) {

}
