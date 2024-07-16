package com.nhnacademy.codequestweb.response.refund;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRefundResponseDto {
    Long paymentId;
    String tossPaymentKey;
    String orderStatus;

}
