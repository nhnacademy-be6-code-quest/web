package com.nhnacademy.codequestweb.response.product.tag;

import lombok.Builder;

@Builder
public record TagGetResponseDto (
        Long tagId,
        String tagName,
        Long productCount
){
}