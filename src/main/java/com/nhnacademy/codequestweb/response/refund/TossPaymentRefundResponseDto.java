package com.nhnacademy.codequestweb.response.refund;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossPaymentRefundResponseDto {
    String cancelReason;
        Long amount;
}
