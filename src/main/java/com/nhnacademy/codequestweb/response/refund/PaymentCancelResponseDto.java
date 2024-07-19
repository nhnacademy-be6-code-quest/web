package com.nhnacademy.codequestweb.response.refund;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCancelResponseDto {
    String tossPaymentKey;
    String orderStatus;

}
