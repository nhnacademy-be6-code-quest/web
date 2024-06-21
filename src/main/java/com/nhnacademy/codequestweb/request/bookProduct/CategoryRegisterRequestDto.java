package com.nhnacademy.codequestweb.request.bookProduct;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record CategoryRegisterRequestDto (
        @NotBlank String categoryName,
        @Nullable String parentCategoryName
)
{}