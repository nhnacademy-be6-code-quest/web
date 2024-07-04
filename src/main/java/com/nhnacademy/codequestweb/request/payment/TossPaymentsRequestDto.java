package com.nhnacademy.codequestweb.request.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TossPaymentsRequestDto {
    private String paymentKey;
    private String orderId;
    private long amount;
}
