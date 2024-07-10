package com.nhnacademy.codequestweb.response.point;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PointAccumulationAdminPageResponseDto {
    Integer pointAccumulationAmount;
    String pointAccumulationHistoryDate;
    String pointAccumulationType;
    long clientId;

}
