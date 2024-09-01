package com.nhnacademy.codequestweb.response.payment.approve.impl;

import com.nhnacademy.codequestweb.response.payment.approve.PaymentApproveResponseDto;
import com.nhnacademy.codequestweb.response.payment.approve.SuccessPaymentOrderInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class NaverPaymentApproveResponseDto implements PaymentApproveResponseDto {

    private SuccessPaymentOrderInfo successPaymentOrderInfo;

    @Override
    public boolean isVirtualAccount() {
        return false;
    }

}
