package com.nhnacademy.codequestweb.domain.entity;

import com.nhnacademy.codequestweb.domain.CouponKind;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CouponType {
    private long couponTypeId;
    private CouponKind couponKind;
}
