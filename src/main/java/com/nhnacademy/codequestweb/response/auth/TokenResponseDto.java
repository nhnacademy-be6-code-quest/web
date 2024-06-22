package com.nhnacademy.codequestweb.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TokenResponseDto {
    private String access;
    private String refresh;
}
