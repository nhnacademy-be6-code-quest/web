package com.nhnacademy.codequestweb.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자가 결제와 관련된 정보를 조작하지 않았는지 검증하기 위해 필요한 속성들입니다. 주문에서 결제로 넘어오는 값입니다.
 * 1) 쿠폰, 포인트 같은 할인 금액을 제외한 순수 결제 금액. 2) 토스에 제공해야 하는 tossOrderId 등이 넘어 옵니다.
 *
 * PaymentOrderRequestDto 와는 다른 페이지에서 불러 오는 값입니다. 같은 Dto 를 써도 되는지 고민해 보면 좋을 것 같습니다.
 *
 * @author 김채호
 * @version 1.0
 */
@Builder
@Getter
public class PaymentOrderValidationRequestDto { // 결제에서 -> 주문으로 : 금액과 관련된 것들을 요청하는 Dto

    // payAmount 를 계산하기 위해 필요한 값들 : 결제 DB에 들어가야 하는 민감한 정보는 primitive type 으로 선언했습니다.
    long orderTotalAmount;
    long discountAmountByPoint;
    long discountAmountByCoupon;

    @NotNull
    String tossOrderId;
}
