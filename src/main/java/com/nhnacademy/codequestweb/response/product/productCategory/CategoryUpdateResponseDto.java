package com.nhnacademy.codequestweb.response.product.productCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CategoryUpdateResponseDto(
        @Pattern(regexp = "^(?!.*,).*$")
        @NotNull
        @NotBlank
        String previousCategoryName,

        @Pattern(regexp = "^(?!.*,).*$")
        @NotNull
        @NotBlank
        String newCategoryName,
        LocalDateTime updateTime
) {
}
