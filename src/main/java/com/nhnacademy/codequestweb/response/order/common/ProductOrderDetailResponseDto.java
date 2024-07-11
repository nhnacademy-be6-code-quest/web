package com.nhnacademy.codequestweb.response.order.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductOrderDetailResponseDto {
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Long pricePerProduct;
    private String productName;
}
