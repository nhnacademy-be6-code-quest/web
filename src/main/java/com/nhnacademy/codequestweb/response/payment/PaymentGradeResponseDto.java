package com.nhnacademy.codequestweb.response.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentGradeResponseDto {
    Long paymentGradeValue;
}