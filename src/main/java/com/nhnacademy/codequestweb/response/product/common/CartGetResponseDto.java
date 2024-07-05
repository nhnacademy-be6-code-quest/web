package com.nhnacademy.codequestweb.response.product.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CartGetResponseDto(
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Length(min = 2)
        String productName,

        @NotNull
        @Min(0)
        Long productPriceStandard,

        @NotNull
        @Min(0)
        Long productPriceSales,

        @NotNull
        @Min(1)
        Long productQuantityOfCart,

        @NotNull
        @Min(0)
        Long productInventory,

        @NotNull
        @NotBlank
        String productThumbnailImage,

        @NotNull(message = "{must.have.category}")
        @Size(min = 1, message = "{must.have.category}")
        @Size(max =10, message = "{too.much.category}")
        Map<Long, String> categoryMapOfIdAndName,

        Map<Long, String> tagMapOfIdAndName

) {
}