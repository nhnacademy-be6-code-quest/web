package com.nhnacademy.codequestweb.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostProcessRequiredPaymentResponseDto { // 후처리에 필요한 데이터.
    private long amount; // 결제금액
    private String paymentMethodName;
}
