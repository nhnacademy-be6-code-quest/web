package com.nhnacademy.codequestweb.request.payment;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentOrderRequestDto2 {

    long orderTotalAmount;
    long discountAmountByCoupon;
    String tossOrderId;

    long clientId;
    long couponId;
    long discountAmountByPoint;
    long accumulatedPoint;
    List<ProductOrderDetailRequestDto> productOrderDetailList;
}