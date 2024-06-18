package com.nhnacademy.codequestweb.request.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
    private List<OrderItem> orderItemList;
}
