package com.nhnacademy.codequestweb.response.mypage;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ClientDeliveryAddressResponseDto {
    private Long clientDeliveryAddressId;
    private String clientDeliveryAddress;
    private String clientDeliveryAddressDetail;
    private String clientDeliveryAddressNickname;
    private int clientDeliveryZipCode;
}
