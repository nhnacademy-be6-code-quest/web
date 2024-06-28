package com.nhnacademy.codequestweb.response.coupon;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nhnacademy.codequestweb.domain.DiscountType;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CouponPolicyResponseDto {

    private long couponPolicyId;
    private String couponPolicyDescription;
    private DiscountType discountType;
    private long discountValue;
    private long minPurchaseAmount;
    private long maxDiscountAmount;
    @Setter
    private  ProductCategoryCouponResponseDto productCategoryCouponResponseDto;
    @Setter
    private  ProductCouponResponseDto productCouponResponseDto;


}