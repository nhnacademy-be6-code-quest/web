package com.nhnacademy.codequestweb.response.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * 결제에 저장하기 위해 토스 페이먼츠에서 받아 오는 값들입니다.
 * orderId 는 토스 페이먼츠에서 받아 오지 않습니다.
 * @author 김채호
 * @version 1.0
 */
@Builder
@Getter
public class PaymentResponseDto {

    long orderId;

    long payAmount;

    @NotNull
    String paymentMethodName;

    @NotNull
    String tossPaymentKey;
}