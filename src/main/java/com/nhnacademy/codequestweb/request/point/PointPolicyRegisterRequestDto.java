package com.nhnacademy.codequestweb.request.point;

import com.nhnacademy.codequestweb.domain.PointPolicyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PointPolicyRegisterRequestDto {

    private PointPolicyType pointPolicyType;
    private Integer pointValue;
}
