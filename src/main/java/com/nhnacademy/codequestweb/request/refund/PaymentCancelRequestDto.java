package com.nhnacademy.codequestweb.request.refund;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentCancelRequestDto {
    private String orderStatus;
    private long orderId;
}

