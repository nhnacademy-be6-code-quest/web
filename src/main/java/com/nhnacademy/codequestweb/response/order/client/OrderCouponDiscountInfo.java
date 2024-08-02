package com.nhnacademy.codequestweb.response.order.client;

import com.nhnacademy.codequestweb.response.coupon.CouponOrderResponseDto;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
public class OrderCouponDiscountInfo {

    private Long couponId;

    // 쿠폰 기본 정보
    private String couponPolicyDescription; // 쿠폰 정책 설명
    private String discountUnit; // 원 or %
    private Long minPurchaseAmount; // 할인 대산 주문 상품들의 총 합의 최소 주문 금액
    private Long maxDiscountAmount; // 최대 할인 금액
    private Long discountValue; // 할인 금액

    // 쿠폰 할인 정보
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
