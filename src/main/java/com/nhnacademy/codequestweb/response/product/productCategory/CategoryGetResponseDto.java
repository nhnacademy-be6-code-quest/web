package com.nhnacademy.codequestweb.response.product.productCategory;


public record CategoryGetResponseDto (
        Long productCategoryId,
        String categoryName,
        ProductCategory parentProductCategory
){
}