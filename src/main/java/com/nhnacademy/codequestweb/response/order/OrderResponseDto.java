package com.nhnacademy.codequestweb.response.order;

import com.nhnacademy.codequestweb.response.order.field.PackageItemDto;
import com.nhnacademy.codequestweb.response.order.field.ProductItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponseDto { // view에 뿌려질 dto
    private List<ProductItemDto> productItemDtoList;
    private List<PackageItemDto> packageItemDtoList;
    private long shippingPrice;
    private long minPurchasePrice;
    private String shippingPolicyName;
}
