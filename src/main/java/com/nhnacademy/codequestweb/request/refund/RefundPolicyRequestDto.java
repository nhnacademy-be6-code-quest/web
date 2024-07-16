package com.nhnacademy.codequestweb.request.refund;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundPolicyRequestDto {

    String refundAndCancelPolicyReason;
    String refundAndCancelPolicyType;
    int refundShippingFee;
}