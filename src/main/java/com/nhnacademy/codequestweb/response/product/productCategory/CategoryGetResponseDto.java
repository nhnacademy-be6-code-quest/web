package com.nhnacademy.codequestweb.response.product.productCategory;

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