package com.nhnacademy.codequestweb.response.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ClientRegisterResponseDto {
    private String clientEmail;
    private LocalDateTime clientCreatedAt;
}
