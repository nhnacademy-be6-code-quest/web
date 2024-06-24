package com.nhnacademy.codequestweb.request.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderPaymentRequestDto {
    Long orderId;
    Long totalPrice;
}