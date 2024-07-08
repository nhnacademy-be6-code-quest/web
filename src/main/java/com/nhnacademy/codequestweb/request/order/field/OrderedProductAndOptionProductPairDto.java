package com.nhnacademy.codequestweb.request.order.field;

import lombok.Builder;

/**
 * OrderedProductAndOptionProductPairDto : 주문한 상품 디테일, 선택한 옵션 상품 리스트
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Builder
public record OrderedProductAndOptionProductPairDto(
        Long productId,
        Long productSinglePrice,
        Long quantity,
        Long optionProductId,
        String optionProductName,
        Long optionProductSinglePrice,
        Long optionQuantity
){
}