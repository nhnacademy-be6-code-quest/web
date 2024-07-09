package com.nhnacademy.codequestweb.request.refund;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundOrderRequestDto {

    LocalDateTime orderDateTime;
    long productTotalAmount;
    long shippingFeeOfOrderDate;
    long orderTotalAmount;
    long discountAmountByCoupon;
    long discountAmountByPoint;
    long accumulatedPoint;
    List<RefundProductOrderDetailRequestDto> refundProductOrderDetailRequestDtoList;
}