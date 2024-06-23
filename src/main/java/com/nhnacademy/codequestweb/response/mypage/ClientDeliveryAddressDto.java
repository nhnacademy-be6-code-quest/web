package com.nhnacademy.codequestweb.response.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClientDeliveryAddressDto {
    private String clientDeliveryAddress;
    private String clientDeliveryAddressDetail;
    private String clientDeliveryAddressNickname;
    private int clientDeliveryZipCode;
}
