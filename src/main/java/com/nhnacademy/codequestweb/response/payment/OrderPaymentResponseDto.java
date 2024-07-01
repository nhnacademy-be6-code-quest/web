package com.nhnacademy.codequestweb.response.payment;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderPaymentResponseDto {
    // payAmount 를 계산하기 위해 필요한 값들
    long orderTotalAmount;          // 주문 총 금액
    long discountAmountByCoupon;    // 쿠폰으로 할인 받은 값
    long discountAmountByPoint;     // 포인트로 할인 받은 값

    // toss 에 제공해야 하는 orderId 를 제공하기 위해 필요한 값
    String tossOrderId;

    // orderName : ex) "초코파이 외 10건" 을 만들어 주기 위해 필요한 정보
    List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList;
}
