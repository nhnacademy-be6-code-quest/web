package com.nhnacademy.codequestweb.response.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentMethodResponseDto {
    long paymentMethodId;
    @NotNull
    String paymentMethodName;
}
