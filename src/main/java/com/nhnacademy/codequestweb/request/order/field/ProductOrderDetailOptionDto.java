package com.nhnacademy.codequestweb.request.order.field;

import lombok.Builder;

@Builder
public record ProductOrderDetailOptionDto(
        long productId,
        String optionProductName,
        long optionProductSinglePrice,
        long quantity
){
}