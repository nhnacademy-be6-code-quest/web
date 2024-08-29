package com.nhnacademy.codequestweb.request.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "pgName"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TossPaymentViewRequestDto.class, name = "toss"),
        @JsonSubTypes.Type(value = NaverPaymentViewRequestDto.class, name = "naver"),
        @JsonSubTypes.Type(value = KakaoPaymentViewRequestDto.class, name = "kakao")
})
public interface PaymentViewRequestDto {
}
