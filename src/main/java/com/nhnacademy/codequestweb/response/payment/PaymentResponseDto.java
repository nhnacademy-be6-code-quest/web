package com.nhnacademy.codequestweb.response.payment;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private Long paymentId;
    private Long orderId;
    private LocalDateTime paymentDate;
    private Long clientDeliveryAddressId;
    private PaymentMethodResponseDto paymentMethod;
    private Long couponId;
}
