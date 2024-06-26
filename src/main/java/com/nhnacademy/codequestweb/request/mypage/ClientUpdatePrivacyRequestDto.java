package com.nhnacademy.codequestweb.request.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdatePrivacyRequestDto {
    private String clientName;
    private LocalDate clientBirth;
}
