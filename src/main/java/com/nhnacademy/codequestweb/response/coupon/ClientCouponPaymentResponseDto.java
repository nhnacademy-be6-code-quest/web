package com.nhnacademy.codequestweb.response.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientCouponPaymentResponseDto {
    private Long clientId;
    private String clientName;
    private String clientEmail;
}
