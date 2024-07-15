package com.nhnacademy.codequestweb.request.point;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointPolicyModifyRequestDto {
    long pointPolicyId;
    Integer pointValue;
}
