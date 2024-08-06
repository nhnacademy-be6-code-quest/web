package com.nhnacademy.codequestweb.response.order.common;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDtoItem {
    Long productId; // 상품 아이디
    String productName; // 상품 이름
    Long quantity; // 수량
    List<Long> categoryIdList; // 상품의 카테고리
    Long productSinglePrice; // 상품 단품 가격
    Boolean packableProduct; // 포장 가능 상품 여부

    Boolean usePackaging; // 포장 선택 여부
    Long optionProductId; // 옵션 상품 아이디
    String optionProductName; // 옵션 상품 이름
    Long optionProductSinglePrice; // 옵션 상품 단품 가격
    Long optionQuantity = 0L;

    @Builder
    public OrderDetailDtoItem(Long productId, String productName, Long quantity, List<Long> categoryIdList, Boolean packableProduct,Long productSinglePrice){
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.categoryIdList = categoryIdList;
        this.packableProduct = packableProduct;
        this.productSinglePrice = productSinglePrice;
    }
}
