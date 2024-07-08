package com.nhnacademy.codequestweb.request.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentUsePointRequestDto {

    long clientId;
    long discountAmountByPoint;
}
