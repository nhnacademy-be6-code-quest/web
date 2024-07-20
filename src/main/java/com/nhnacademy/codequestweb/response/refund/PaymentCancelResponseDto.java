package com.nhnacademy.codequestweb.response.refund;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelResponseDto {
    Long paymentId;
    String tossPaymentKey;
    String orderStatus;
}
