package com.nhnacademy.codequestweb.request.order.client;

import com.nhnacademy.codequestweb.domain.OrderStatus;
import lombok.Builder;

@Builder
public record ClientOrderPutRequestDto (
    OrderStatus orderStatus
)
{

}
