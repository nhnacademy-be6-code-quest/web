package com.nhnacademy.codequestweb.request.payment.viewRequest.impl;

import com.nhnacademy.codequestweb.request.payment.viewRequest.PaymentViewRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentViewRequestDto implements PaymentViewRequestDto {
    private long amount;
    private String orderCode;
    private String orderName;
}
