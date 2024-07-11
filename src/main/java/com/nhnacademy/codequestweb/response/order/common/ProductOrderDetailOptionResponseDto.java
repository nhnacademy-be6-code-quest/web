package com.nhnacademy.codequestweb.response.order.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductOrderDetailOptionResponseDto {
    private Long productId;
    private Long productOrderDetailId;
    private String optionProductName;
    private Long optionProductPrice;
    private Long optionProductQuantity;
}
