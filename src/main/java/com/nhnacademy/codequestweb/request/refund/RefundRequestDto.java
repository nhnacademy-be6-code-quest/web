package com.nhnacademy.codequestweb.request.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
public class RefundRequestDto {
     long orderId;
     String refundDetailReason;
     Long refundPolicyId;
}
