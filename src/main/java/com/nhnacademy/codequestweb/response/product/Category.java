package com.nhnacademy.codequestweb.response.product;

public record Category(
        String categoryName,
        Category parentCategory
) {
}
