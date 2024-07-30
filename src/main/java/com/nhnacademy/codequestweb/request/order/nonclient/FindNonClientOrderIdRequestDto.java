package com.nhnacademy.codequestweb.request.order.nonclient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class FindNonClientOrderIdRequestDto {
    String ordererName;
    String phoneNumber;
    String email;
}