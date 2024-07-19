package com.nhnacademy.codequestweb.response.point;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TotalPointAmountResponseDto {
    Long totalPoint;
    Long pointAccumulationRate;
}
