package com.nhnacademy.codequestweb.request.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductOrderDetailOptionRequestDto {
    long productId;
    long optionProductQuantity;
}