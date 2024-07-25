package com.nhnacademy.codequestweb.response.product.product_category;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CategoryRegisterResponseDto (
        @Pattern(regexp = "^(?!.*,).*$")
        @NotNull
        @NotBlank
        String categoryName,

        @Nullable
        String parentCategoryName
) {}
