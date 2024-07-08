package com.nhnacademy.codequestweb.request.payment;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentOrderApproveRequestDto {

    long orderTotalAmount;
    long discountAmountByCoupon;
    String tossOrderId;

    Long clientId;
    Long couponId;
    long discountAmountByPoint;
    long accumulatedPoint;
    List<ProductOrderDetailRequestDto> productOrderDetailList;
}