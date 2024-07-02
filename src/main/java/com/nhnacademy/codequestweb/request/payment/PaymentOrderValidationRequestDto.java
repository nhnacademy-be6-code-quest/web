package com.nhnacademy.codequestweb.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자가 결제와 관련된 정보를 조작하지 않았는지 검증하기 위해 필요한 속성들입니다.
 * 주문에서 결제로 넘어오는 값입니다.
 * @author 김채호
 * @version 1.0
 */
@Builder
@Getter
public class PaymentOrderValidationRequestDto { // 결제에서 -> 주문으로 : 금액과 관련된 것들을 요청하는 Dto
    @NotNull
    long orderTotalAmount;

    @NotNull
    long discountAmountByPoint;

    @NotNull
    long discountAmountByCoupon;

    @NotNull
    String tossOrderId;
}
