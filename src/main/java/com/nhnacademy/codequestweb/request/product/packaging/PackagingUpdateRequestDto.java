package com.nhnacademy.codequestweb.request.product.packaging;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PackagingUpdateRequestDto {
    @NotNull
    Long productId;

    @NotBlank
    @NotNull
    private String packageName;

    @NotBlank
    @NotNull
    private String productName;

    @NotBlank
    @NotNull
    String productThumbnailUrl;

    @NotBlank
    @NotNull
    String productDescription;

    @Min(0)
    long productPriceStandard;

    @Min(0)
    long productPriceSales;

    @Min(0)
    long productInventory;

    @Min(0)
    int productState;
}
