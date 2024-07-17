package com.nhnacademy.codequestweb.response.product.tag;

public record TagGetResponseDto (
        Long tagId,
        String tagName,
        Long productCount
){
}