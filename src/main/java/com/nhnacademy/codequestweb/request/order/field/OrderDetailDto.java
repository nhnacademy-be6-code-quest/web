package com.nhnacademy.codequestweb.request.order.field;

import lombok.Builder;

@Builder
public record OrderDetailDto(
    long productId,
    long price,
    long quantity
)
{

}