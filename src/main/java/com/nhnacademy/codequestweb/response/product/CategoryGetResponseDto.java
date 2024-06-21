package com.nhnacademy.codequestweb.response.product;

public record CategoryGetResponseDto (
        String categoryName,
        Category parentCategory
){
}