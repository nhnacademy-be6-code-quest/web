package com.nhnacademy.codequestweb.request.product.bookProduct;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record BookProductUpdateRequestDto (
        @NotNull
        Long bookId,

        @NotNull
        @Length(min = 2)
        String productName,
        boolean packable,

        @NotNull
        @NotBlank
        String productDescription,

        @Min(0)
        long productPriceSales,

        @Min(0)
        long productInventory,

        @Min(0)
        int productState,

        @NotNull(message = "{must.have.category}")
        @Size(min = 1, message = "{must.have.category}")
        @Size(max =10, message = "{}")
        List<String> categories,
        List<String> tags
)
{
}
