package com.nhnacademy.codequestweb.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductStateUpdateRequestDto(
        @NotNull
        @Min(1)
        Long productId,

        @NotNull
        @Min(0)
        Integer productState
){
}
