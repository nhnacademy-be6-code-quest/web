package com.nhnacademy.codequestweb.response.product;

public record CategoryRegisterResponseDto (
        String categoryName,
        String parentCategoryName
) {}
