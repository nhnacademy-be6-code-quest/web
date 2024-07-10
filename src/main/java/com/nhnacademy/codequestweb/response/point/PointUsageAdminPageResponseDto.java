package com.nhnacademy.codequestweb.response.point;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PointUsageAdminPageResponseDto {
    String pointUsageHistoryDate;
    Integer pointUsageAmount;
    long clientId;
    String pointUsageType;
}