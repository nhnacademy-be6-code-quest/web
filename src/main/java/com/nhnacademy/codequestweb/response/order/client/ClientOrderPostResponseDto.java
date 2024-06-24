package com.nhnacademy.codequestweb.response.order.client;

import lombok.Builder;

@Builder
public record ClientOrderPostResponseDto (
        long orderId,
        long totalProductPrice
){

}