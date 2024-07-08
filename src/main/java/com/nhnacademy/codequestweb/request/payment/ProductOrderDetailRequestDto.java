package com.nhnacademy.codequestweb.request.payment;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductOrderDetailRequestDto {
    long productId;
    long quantity;
    List<ProductOrderDetailOptionRequestDto> productOrderDetailOptionRequestDtoList;
}