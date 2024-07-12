package com.nhnacademy.codequestweb.request.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientUpdateGradeRequestDto {
    private Long clientId;
    private Long payment;
}