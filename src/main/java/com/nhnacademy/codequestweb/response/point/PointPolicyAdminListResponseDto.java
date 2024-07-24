package com.nhnacademy.codequestweb.response.point;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PointPolicyAdminListResponseDto {
    String pointAccumulationType;
    Long pointValue;
    String pointPolicyCreationDate;
    String pointStatus;
    long pointPolicyId;
}
