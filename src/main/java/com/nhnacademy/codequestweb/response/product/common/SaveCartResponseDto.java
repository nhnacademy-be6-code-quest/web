package com.nhnacademy.codequestweb.response.product.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SaveCartResponseDto(
        @NotNull
        @Min(0)
        Long savedCartQuantity
) {
}
