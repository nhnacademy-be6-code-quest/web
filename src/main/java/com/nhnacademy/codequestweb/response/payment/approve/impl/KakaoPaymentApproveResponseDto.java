package com.nhnacademy.codequestweb.response.payment.approve.impl;

import com.nhnacademy.codequestweb.response.payment.approve.PaymentApproveResponseDto;
import com.nhnacademy.codequestweb.response.payment.approve.SuccessPaymentOrderInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoPaymentApproveResponseDto implements PaymentApproveResponseDto {

    private SuccessPaymentOrderInfo successPaymentOrderInfo;

    @Override
    public boolean isVirtualAccount() {
        return false;
    }

}
