package com.nhnacademy.codequestweb.request.payment;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class ApprovePaymentRequestDto {
    String orderCode;
    String pgName;
    Map<String, String[]> reqParamMap;
}
