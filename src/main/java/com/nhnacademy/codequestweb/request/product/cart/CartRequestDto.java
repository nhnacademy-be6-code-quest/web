package com.nhnacademy.codequestweb.request.product.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartRequestDto(
        @NotNull
        @Min(1)
        Long clientId,

        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Min(1)
        Long quantity
)
{ }