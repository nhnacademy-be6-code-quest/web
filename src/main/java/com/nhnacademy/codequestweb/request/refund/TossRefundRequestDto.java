package com.nhnacademy.codequestweb.request.refund;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossRefundRequestDto {
    String cancelReason;

}
