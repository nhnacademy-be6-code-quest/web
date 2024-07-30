package com.nhnacademy.codequestweb.request.order.nonclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UpdateNonClientOrderPasswordRequestDto {
    String ordererName;
    String phoneNumber;
    String email;
    String newPassword;
}
