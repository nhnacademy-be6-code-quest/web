package com.nhnacademy.codequestweb.response.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderPaymentResponseDto {
    Long orderId;
    Long totalPrice;
}