package com.nhnacademy.codequestweb.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequestDto {
    @NotNull
    private Long orderId;                   // 주문 아이디

    @NotNull
    private Long clientDeliveryAddressId;   // 회원 배송지 아이디

    private Long paymentMethodId;           // 결제 수단의 아이디

    private Long couponId;                  // 이번 결제에서 사용할 쿠폰의 아이디
}
