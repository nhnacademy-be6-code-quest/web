package com.nhnacademy.codequestweb.response.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClientPhoneNumberResponseDto {
    private Long clientNumberId;
    private String clientPhoneNumber;
}
