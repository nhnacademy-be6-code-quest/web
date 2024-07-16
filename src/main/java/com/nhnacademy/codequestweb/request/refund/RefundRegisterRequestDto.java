package com.nhnacademy.codequestweb.request.refund;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter

public class RefundRegisterRequestDto {
    private String cancelReason;
    private long paymentId;
    private String orderStatus;
    private long orderId;
}
