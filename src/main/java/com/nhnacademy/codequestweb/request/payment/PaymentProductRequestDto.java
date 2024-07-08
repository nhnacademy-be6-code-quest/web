package com.nhnacademy.codequestweb.request.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentProductRequestDto {
    long productId;
    long quantity;
}
