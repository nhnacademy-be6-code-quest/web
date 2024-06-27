package com.nhnacademy.codequestweb.response.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponseDto {
    private long couponId;
    private CouponTypeResponseDto couponType;
    private CouponPolicyResponseDto couponPolicy;
    private LocalDateTime expirationDate;
    private LocalDateTime usedDate; // TODO : 필요 없으면 지우기
    private Status status;
}