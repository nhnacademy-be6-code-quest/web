package com.nhnacademy.codequestweb.request.refund;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundPolicyRegisterRequestDto {
    private String refundPolicyType;
    private Integer refundShippingFee;

}
