package com.nhnacademy.codequestweb.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductLikeRequestDto (
        @NotNull @Min(1) Long clientId,
        @NotNull @Min(1) Long productId
){
}
