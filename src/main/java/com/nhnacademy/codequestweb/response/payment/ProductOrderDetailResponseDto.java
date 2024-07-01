package com.nhnacademy.codequestweb.response.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductOrderDetailResponseDto {
    long productId;
    long quantity;
    long pricePerProduct;
    String productName;
}
