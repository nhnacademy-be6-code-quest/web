package com.nhnacademy.codequestweb.request.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPaymentViewRequestDto implements PaymentViewRequestDto{
    private String paymentId;
    private String redirectUrl;
}
