package com.nhnacademy.codequestweb.request.payment.viewRequest.impl;

import com.nhnacademy.codequestweb.request.payment.viewRequest.PaymentViewRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPaymentViewRequestDto implements PaymentViewRequestDto {
    private String paymentId;
    private String redirectUrl;
}
