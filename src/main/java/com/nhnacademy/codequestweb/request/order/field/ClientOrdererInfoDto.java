package com.nhnacademy.codequestweb.request.order.field;

public record ClientOrdererInfoDto(
        String ordererName,
        String phoneNumber,
        String zipCode,
        String address,
        String detailAddress
){
}
