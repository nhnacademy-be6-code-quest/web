package com.nhnacademy.codequestweb.request.product.bookProduct;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record BookProductRegisterRequestDto(
        @NotBlank
        @NotNull
        String title,

        @NotNull
        String publisher,

        @NotNull
        String author,

        @NotNull
        LocalDate pubDate,

        @NotNull
        @Length(min = 10, max =10) String isbn,

        @Length(min = 13, max =13) String isbn13,

        @NotNull
        String cover,

        @NotNull
        @NotBlank
        @Length(min = 2)
        String productName,

        boolean packable,

        @NotNull
        String productDescription,

        @Min(0)
        long productPriceStandard,

        @Min(0)
        long productPriceSales,

        @Min(0)
        long productInventory,

        @NotNull(message = "{must.have.category}")
        @Size(min = 1, message = "{must.have.category}")
        @Size(max =10, message = "{}")
        Set<String> categories,
        Set<String> tags
) {}
