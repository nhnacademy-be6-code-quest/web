package com.nhnacademy.codequestweb.response.coupon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CouponMyPageCouponResponseDto {

    String issuedDate;
    String usedDate;
    String expirationDate;
    String status;
    CouponPolicy couponPolicy;
    String couponKind;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CouponPolicy {

        private String couponPolicyDescription;
        private String discountType;
        private long discountValue;
        private long minPurchaseAmount;
        private long maxDiscountAmount;
    }

}
