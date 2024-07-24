package com.nhnacademy.codequestweb.request.product.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InventorySetRequestDto(
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Min(1)
        Long quantityToSet
) {
}