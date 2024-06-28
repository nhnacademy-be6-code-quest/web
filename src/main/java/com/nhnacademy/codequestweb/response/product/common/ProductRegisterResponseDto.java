package com.nhnacademy.codequestweb.response.product.common;

import java.time.LocalDateTime;

public record ProductRegisterResponseDto(
        Long id,
        LocalDateTime registerTime
){}