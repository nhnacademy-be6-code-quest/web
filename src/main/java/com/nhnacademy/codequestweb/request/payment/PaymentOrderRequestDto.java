package com.nhnacademy.codequestweb.request.payment;

import com.nhnacademy.codequestweb.response.payment.ProductOrderDetailResponseDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 토스 페이먼츠에 결제와 관련된 정보를 넘기기 위한 정보들입니다.
 * 주문에서 결제로 넘어오는 값입니다.
 * @author 김채호
 * @version 1.0
 */
@Builder
@Getter
public class PaymentOrderRequestDto {
    // payAmount 를 계산하기 위해 필요한 값들
    long orderTotalAmount;          // 주문 총 금액
    long discountAmountByCoupon;    // 쿠폰으로 할인 받은 값
    long discountAmountByPoint;     // 포인트로 할인 받은 값

    // toss 에 제공해야 하는 orderId 를 제공하기 위해 필요한 값
    @NotNull
    String tossOrderId;

    // orderName : ex) "초코파이 외 10건" 을 만들어 주기 위해 필요한 정보
    @NotNull
    List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList;
}
