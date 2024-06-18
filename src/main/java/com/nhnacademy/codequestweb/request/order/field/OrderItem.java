package com.nhnacademy.codequestweb.request.order.field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItem {
    private long productId;
    private long quantity;
}
