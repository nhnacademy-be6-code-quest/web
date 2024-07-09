package com.nhnacademy.codequestweb.controller.refund;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundPolicyRequestDto {

    String refundAndCancelPolicyReason;
    String refundAndCancelPolicyType;
    int refundShippingFee;
}