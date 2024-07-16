package com.nhnacademy.codequestweb.request.refund;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundTossRequestDto {
    private String cancelReason;
    private String tossPaymentKey;
    private long paymentId;
    private String orderStatus;

}
