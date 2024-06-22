package com.nhnacademy.codequestweb.response.coupon;

import com.nhnacademy.codequestweb.domain.Status;

import com.nhnacademy.codequestweb.temp.CouponPolicy;
import com.nhnacademy.codequestweb.temp.CouponType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponseDto {
    private long couponId;
    private CouponType couponType;
    private CouponPolicy couponPolicy;
    private LocalDateTime issuedDate;
    private long clientId;
    private LocalDateTime expirationDate;
    private LocalDateTime usedDate;
    private Status status;
}