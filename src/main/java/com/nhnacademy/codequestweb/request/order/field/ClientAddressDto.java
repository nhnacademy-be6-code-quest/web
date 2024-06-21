package com.nhnacademy.codequestweb.request.order.field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientAddressDto {
    private String addressNickname; // 주소별칭
    private String address; // 도로명 주소
    private String detailAddress; // 상세주소
    private String zipCode; // 우편번호
}
