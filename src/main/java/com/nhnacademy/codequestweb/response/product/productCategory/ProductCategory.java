package com.nhnacademy.codequestweb.response.product.productCategory;

public record ProductCategory(
        Long productCategoryId,
        String categoryName,
        ProductCategory parentProductCategory
) {
}
