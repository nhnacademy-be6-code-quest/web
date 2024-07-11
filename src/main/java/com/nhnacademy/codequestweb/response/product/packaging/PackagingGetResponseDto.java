package com.nhnacademy.codequestweb.response.product.packaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PackagingGetResponseDto {
    private Long packagingId;
    private String packagingName;
    private String packagingImage;

    private Long productId;
    private String productName;
    private String productDescription;
    private int productState;
    private long productPriceStandard;
    private long productPriceSales;
    private long productInventory;
}
