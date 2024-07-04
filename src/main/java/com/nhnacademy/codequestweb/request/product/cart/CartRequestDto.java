package com.nhnacademy.codequestweb.request.product.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CartRequestDto(
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Min(1)
        Long quantity
)
{ }