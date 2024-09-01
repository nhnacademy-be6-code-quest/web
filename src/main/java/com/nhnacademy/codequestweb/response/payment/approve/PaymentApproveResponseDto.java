package com.nhnacademy.codequestweb.response.payment.approve;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nhnacademy.codequestweb.response.payment.approve.impl.KakaoPaymentApproveResponseDto;
import com.nhnacademy.codequestweb.response.payment.approve.impl.NaverPaymentApproveResponseDto;
import com.nhnacademy.codequestweb.response.payment.approve.impl.TossPaymentApproveResponseDto;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "pgName"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TossPaymentApproveResponseDto.class, name = "toss"),
        @JsonSubTypes.Type(value = NaverPaymentApproveResponseDto.class, name = "naver"),
        @JsonSubTypes.Type(value = KakaoPaymentApproveResponseDto.class, name = "kakao")
})
public interface PaymentApproveResponseDto {

    default boolean isVirtualAccount(){
        return getSuccessPaymentOrderInfo().isVirtualAccount();
    }

    SuccessPaymentOrderInfo getSuccessPaymentOrderInfo();

}
