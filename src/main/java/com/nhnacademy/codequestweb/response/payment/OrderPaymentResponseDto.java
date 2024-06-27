package com.nhnacademy.codequestweb.response.payment;

import java.util.List;
import lombok.Builder;

@Builder
public class OrderPaymentResponseDto {
    long totalPrice;
    List<ProductOrderDetailResponseDto> productOrderDetailDtoList;
}
