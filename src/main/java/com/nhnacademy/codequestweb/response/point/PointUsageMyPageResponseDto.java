package com.nhnacademy.codequestweb.response.point;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PointUsageMyPageResponseDto {
    String pointUsageHistoryDate;
    Integer pointUsageAmount;
    String pointUsageType;
}
