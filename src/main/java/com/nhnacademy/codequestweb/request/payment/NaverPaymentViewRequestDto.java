package com.nhnacademy.codequestweb.request.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverPaymentViewRequestDto implements PaymentViewRequestDto{
    String productName;
    String orderCode;
    Long amount;
    Long taxScopeAmount;
    Long taxExScopeAmount;
}
