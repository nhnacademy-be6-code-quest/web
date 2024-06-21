package com.nhnacademy.codequestweb.response.product;

import java.time.LocalDateTime;

public record BookProductRegisterResponseDto(
        Long id,
        LocalDateTime registerTime
){}