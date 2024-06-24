package com.nhnacademy.codequestweb.response.order.field;

import lombok.Builder;

@Builder
public record PhoneNumberDto (
    String alias,
    String phoneNumber
)
{

}
