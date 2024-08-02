package com.nhnacademy.codequestweb.response.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PaymentMethodResponseDto {
    private Long paymentMethodTypeId;
    private String paymentMethodTypeName;
}
