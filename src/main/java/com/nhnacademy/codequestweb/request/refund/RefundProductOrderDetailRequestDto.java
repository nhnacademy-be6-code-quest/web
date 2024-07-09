package com.nhnacademy.codequestweb.request.refund;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundProductOrderDetailRequestDto {
    long productId;
    long quantity;
    long pricePerProduct;
    String productName;
    List<RefundProductOrderDetailOptionRequestDto> refundProductOrderDetailOptionRequestDtoList;
}