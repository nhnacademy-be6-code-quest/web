package com.nhnacademy.codequestweb.request.point;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PointPolicyActiveRequestDto {
    long pointPolicyId;
    String pointAccumulationType;
}
