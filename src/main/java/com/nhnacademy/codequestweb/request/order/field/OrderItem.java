package com.nhnacademy.codequestweb.request.order.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private long productId;
    private long quantity;
}