package com.nhnacademy.codequestweb.request.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentAccumulatePointRequestDto {

    long clientId;
    long accumulatedPoint;
}
