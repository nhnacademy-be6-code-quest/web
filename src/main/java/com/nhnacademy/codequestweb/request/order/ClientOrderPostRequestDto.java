package com.nhnacademy.codequestweb.request.order;

import com.nhnacademy.codequestweb.request.order.field.ClientAddressDto;
import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.request.order.field.PhoneNumberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderPostRequestDto {
    private List<OrderItemDto> orderItemDtoList;
}
