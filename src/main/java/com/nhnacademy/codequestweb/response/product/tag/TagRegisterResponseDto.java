package com.nhnacademy.codequestweb.response.product.tag;

import lombok.Builder;

@Builder
public record TagRegisterResponseDto(
        Long tagId,
        String tagName
) {}
