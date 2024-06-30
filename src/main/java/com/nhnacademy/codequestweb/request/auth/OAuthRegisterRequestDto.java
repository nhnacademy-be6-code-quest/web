package com.nhnacademy.codequestweb.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthRegisterRequestDto {
    private String access;
    private String name;
    private LocalDate birth;
}
