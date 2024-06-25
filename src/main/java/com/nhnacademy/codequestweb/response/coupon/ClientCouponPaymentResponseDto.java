package com.nhnacademy.codequestweb.response.coupon;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientCouponPaymentResponseDto {
    private Long clientId;
    private String clientName;
    private String clientEmail;
}
