package com.nhnacademy.codequestweb.request.payment.viewRequest.impl;

import com.nhnacademy.codequestweb.request.payment.viewRequest.PaymentViewRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverPaymentViewRequestDto implements PaymentViewRequestDto {
    String productName;
    String orderCode;
    Long amount;
    Long taxScopeAmount;
    Long taxExScopeAmount;
}
