package com.nhnacademy.codequestweb.request.refund;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefundPolicyRegisterRequestDto {
    private String refundPolicyType;
    private Integer refundShippingFee;

}
