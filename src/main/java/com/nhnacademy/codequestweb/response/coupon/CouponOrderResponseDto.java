package com.nhnacademy.codequestweb.response.coupon;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CouponOrderResponseDto {
    long couponId;
    CouponPolicy couponPolicy;
    ProductCoupon productCoupon;
    CategoryCoupon categoryCoupon;

    @Setter
    @NoArgsConstructor
    @Getter
    public static class CouponPolicy {
        String couponPolicyDescription;
        String discountType;
        long discountValue;
        long minPurchaseAmount;
        long maxDiscountAmount;    }
    @Setter
    @Getter
    @NoArgsConstructor
    public static class ProductCoupon {
        Long productId;
    }
    @Setter
    @Getter
    @NoArgsConstructor
    public static class CategoryCoupon {
        Long productCategoryId;
    }

}