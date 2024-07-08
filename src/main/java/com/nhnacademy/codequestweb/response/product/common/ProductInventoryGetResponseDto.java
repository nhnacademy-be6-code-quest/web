package com.nhnacademy.codequestweb.response.product.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductInventoryGetResponseDto (
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Min(0)
        Long productInventory
)
{}
