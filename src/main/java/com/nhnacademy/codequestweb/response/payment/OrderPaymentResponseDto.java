package com.nhnacademy.codequestweb.response.payment;

import java.util.List;
import lombok.Builder;

/*
    TODO: 주문에서 결제로 넘어와야 하는 값
        - 구매자의 고유 아이디 (Auto Increment, Email, Phone Number 등 유추할 수 있는 값이면 안 됨.)
        - 회원 주문인지, 비회원 주문인지 식별할 수 있는 값
        - 최종적으로 결제해야 하는 값 (order_total_amount, discount_amount_by_coupon, discount_amount_by_point)
        - 결제한 상품 이름 1개 및 총 주문 개수 ex) "NHN Academy 에서 살아남기 외 2권"
        - successUrl, failUrl
        - 결제자 이메일
        - 결제자 이름
        - 결제자 휴대폰 번호 (주문자 휴대폰 번호가 아님)
 */

@Builder
public class OrderPaymentResponseDto {
    // TODO: 구매자의 고유 아이디 고민해 보기 (아직 안 들어 감)
    List<ProductOrderDetailResponseDto> productOrderDetailDtoList;
}
