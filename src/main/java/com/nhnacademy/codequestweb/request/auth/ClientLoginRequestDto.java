package com.nhnacademy.codequestweb.request.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientLoginRequestDto {
    private String clientEmail;
    private String clientPassword;
}
