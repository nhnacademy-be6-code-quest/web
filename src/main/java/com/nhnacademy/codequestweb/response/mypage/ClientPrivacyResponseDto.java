package com.nhnacademy.codequestweb.response.mypage;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClientPrivacyResponseDto {
    private String clientGrade;
    private String clientEmail;
    private String clientName;
    private LocalDate clientBirth;
}
