package com.nhnacademy.codequestweb.response.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductOrderDetailResponseDto {
    long productId;
    long quantity;
    long pricePerProduct;
    @NotNull
    String productName;
}
