package com.nhnacademy.codequestweb.response.product.product_category;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;

@Builder
public record CategoryGetResponseDto  (
        Long productCategoryId,
        String categoryName,
        ProductCategory parentProductCategory,
        List<ProductCategory> childrenProductCategory
)implements Serializable{
}