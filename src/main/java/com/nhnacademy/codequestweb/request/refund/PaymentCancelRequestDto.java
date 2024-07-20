package com.nhnacademy.codequestweb.request.refund;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PaymentCancelRequestDto {
    private String orderStatus;
    private long orderId;
    private String cancelReason;
    private String paymentKey;
}
