package com.nhnacademy.codequestweb.request.order.client;


import com.nhnacademy.codequestweb.request.order.field.OrderDetailDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ClientOrderPostRequestDto (
    long clientId,
    List<OrderDetailDto> orderDetailDtoList,
    LocalDateTime deliveryDate,
    long totalPrice,
    int shippingFee,
    String phoneNumber,
    String deliveryAddress
)
{

}