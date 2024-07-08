package com.nhnacademy.codequestweb.response.order.client;

import com.nhnacademy.codequestweb.response.order.field.PackageItemDto;
import com.nhnacademy.codequestweb.response.order.field.ProductItemDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ClientViewOrderPostResponseDto (
       // 상품관련
       List<ProductItemDto> productItemDtoList, // 사용자가 주문하려는 상품들
       List<PackageItemDto> packageItemDtoList // 데이터베이스에 등록된 '포장지' 목록
)
{

}
