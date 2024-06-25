package com.nhnacademy.codequestweb.request.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPaymentRequestDto {
    Long orderId;
    Long totalPrice;
}