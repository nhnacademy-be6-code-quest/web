package com.nhnacademy.codequestweb.response.refund;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundPolicyListResponseDto {
    private String refundPolicyType;
    private int refundShippingFee;
    private String refundPolicyIssuedDate;
    private String refundPolicyExpirationDate;
    private String refundPolicyStatus;
}
