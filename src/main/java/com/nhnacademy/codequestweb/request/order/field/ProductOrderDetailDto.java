package com.nhnacademy.codequestweb.request.order.field;

import lombok.Builder;

@Builder
public record ProductOrderDetailDto(
        long productId,
        long productSinglePrice,
        long quantity
){
}
