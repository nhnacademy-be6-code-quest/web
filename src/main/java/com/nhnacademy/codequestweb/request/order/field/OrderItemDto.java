package com.nhnacademy.codequestweb.request.order.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private Long quantity;
    private List<Long> categoryIdList;
    private Long bookId;
}
