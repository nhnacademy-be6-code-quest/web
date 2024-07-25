package com.nhnacademy.codequestweb.request.payment;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class TossApprovePaymentRequest {
    String orderId; // 토스 오더 아이디
    long amount;
    String paymentKey;
    String methodType;
}
