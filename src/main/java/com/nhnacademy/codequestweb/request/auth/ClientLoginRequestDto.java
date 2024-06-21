package com.nhnacademy.codequestweb.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientLoginRequestDto {
    private String clientEmail;
    private String clientPassword;
}
