package com.nhnacademy.codequestweb.request.mypage;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientRegisterPhoneNumberRequestDto {
    @NotNull
    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Size(min = 10, max = 11, message = "전화번호는 '-'없이 10 또는 11자리의 수 이어야합니다.")
    @Digits(integer = 11, fraction = 0)
    private String phoneNumber;
}
