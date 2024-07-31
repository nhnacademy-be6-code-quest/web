package com.nhnacademy.codequestweb.response.order.client;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
public class OrderCouponDiscountInfo {

    private Long couponId;
    private Boolean isApplicable; // 쿠폰 적용 가능 여부
    private String notApplicableDescription; // 사용 불가 사유

    private Long discountTotalAmount;

    @Builder
    public OrderCouponDiscountInfo(Long couponId, Boolean isApplicable, Long discountTotalAmount) {
        this.couponId = couponId;
        this.isApplicable = isApplicable;
        this.discountTotalAmount = discountTotalAmount;
    }

    public void updateIsApplicable(Boolean isApplicable) {
        this.isApplicable = isApplicable;
    }

    public void updateNotApplicableDescription(String notApplicableDescription){
        this.notApplicableDescription = notApplicableDescription;
    }

}
