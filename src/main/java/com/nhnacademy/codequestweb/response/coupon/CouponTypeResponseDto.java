package com.nhnacademy.codequestweb.response.coupon;


import com.nhnacademy.codequestweb.domain.CouponKind;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponTypeResponseDto {
    long couponTypeId;
    CouponKind couponKind;

}
