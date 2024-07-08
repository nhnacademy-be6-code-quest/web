package com.nhnacademy.codequestweb.response.coupon;

import lombok.Builder;

@Builder
public record ProductGetResponseDto(
        Long productId,
        String productName,
        int productState,
        long productPriceStandard,
        long productPriceSales,
        String productThumbNailImage
){
}
