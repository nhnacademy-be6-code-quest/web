package com.nhnacademy.codequestweb.request.product.tag;

import lombok.Builder;

@Builder
public record TagUpdateRequestDto (
        String currentTagName,
        String newTagName
)
{}