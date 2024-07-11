package com.nhnacademy.codequestweb.response.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRoleResponseDto {
    private List<String> roles;
}
