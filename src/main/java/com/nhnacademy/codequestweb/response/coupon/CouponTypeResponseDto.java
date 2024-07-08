package com.nhnacademy.codequestweb.response.coupon;


import com.nhnacademy.codequestweb.domain.CouponKind;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponTypeResponseDto {
    long couponTypeId;
    CouponKind couponKind;

    public CouponTypeResponseDto(long couponTypeId, CouponKind couponKind) {
        this.couponTypeId = couponTypeId;
        this.couponKind = couponKind;
    }
}
