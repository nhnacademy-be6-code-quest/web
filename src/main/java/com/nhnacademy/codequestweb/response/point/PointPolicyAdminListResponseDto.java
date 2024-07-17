package com.nhnacademy.codequestweb.response.point;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PointPolicyAdminListResponseDto {
    String pointAccumulationType;
    Integer pointValue;
    String pointPolicyCreationDate;
    String pointStatus;
    long pointPolicyId;

}
