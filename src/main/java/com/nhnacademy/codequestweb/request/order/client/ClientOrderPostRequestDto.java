package com.nhnacademy.codequestweb.request.order.client;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nhnacademy.codequestweb.deserializer.DateToLocalDateTimeDeserializer;
import com.nhnacademy.codequestweb.request.order.field.OrderDetailDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ClientOrderPostRequestDto (
    long clientId,
    List<OrderDetailDto> orderDetailDtoList,
    //@JsonDeserialize(using = DateToLocalDateTimeDeserializer.class)
    LocalDateTime deliveryDate,
    long totalPrice,
    int shippingFee,
    String phoneNumber,
    String deliveryAddress
)
{

}