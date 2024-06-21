package com.nhnacademy.codequestweb.request.bookProduct;

import jakarta.validation.constraints.NotBlank;

public record TagRegisterRequestDto(
       @NotBlank String tagName
){}
