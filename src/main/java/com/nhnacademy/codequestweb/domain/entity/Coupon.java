package com.nhnacademy.codequestweb.domain.entity;

import com.nhnacademy.codequestweb.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class Coupon {

    private long couponId;
    private CouponType couponType;
    private CouponPolicy couponPolicy;
    private long clientId;
    private LocalDateTime issuedDate;
    private LocalDateTime expirationDate;
    private LocalDateTime usedDate;
    private Status status;



}
