package com.nhnacademy.codequestweb.response.order.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClientOrderDiscountForm {
    Long payAmount; // 실제 결제할 가격
    Long couponId; // 적용한 쿠폰
    Long couponDiscountAmount; // 쿠폰 할인 금액
    Long usedPointDiscountAmount; // 포인트 사용 금액

    @Builder
    public ClientOrderDiscountForm(Long payAmount){
        this.payAmount = payAmount;
        usedPointDiscountAmount = 0L;
        couponDiscountAmount = 0L;
    }
}
