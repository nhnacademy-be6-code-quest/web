package com.nhnacademy.codequestweb.request.order.field;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemDto{
    Long productId;
    Long quantity;
    List<Long> categoryIdList;
    boolean packable;
}