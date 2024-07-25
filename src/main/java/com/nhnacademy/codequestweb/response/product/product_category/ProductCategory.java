package com.nhnacademy.codequestweb.response.product.product_category;

import java.io.Serializable;
import lombok.Builder;

@Builder
public record ProductCategory (
        Long productCategoryId,
        String categoryName,
        ProductCategory parentProductCategory
) implements Serializable{
}
