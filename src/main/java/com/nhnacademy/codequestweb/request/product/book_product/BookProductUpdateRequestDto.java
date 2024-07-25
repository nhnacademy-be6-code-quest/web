package com.nhnacademy.codequestweb.request.product.book_product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookProductUpdateRequestDto {
        @NotNull
        Long productId;

        @NotNull
        @Length(min = 2)
        String productName;

        boolean packable;

        @Length(min = 10, max =10) String isbn;

        @Length(min = 13, max =13) String isbn13;

        String cover;

        @NotNull
        @NotBlank
        String productDescription;

        @Min(0)
        long productPriceSales;

        @Min(0)
        long productInventory;

        @Min(0)
        int productState;

        @NotNull(message = "{must.have.category}")
        @Size(min = 1, message = "{must.have.category}")
        @Size(max =10, message = "{}")
        Set<String> categories;
        Set<String> tags;
}
