package com.nhnacademy.codequestweb.controller.refund;

import com.nhnacademy.codequestweb.request.refund.RefundOrderRequestDto;
import com.nhnacademy.codequestweb.service.refund.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
        <상품 개수 별로>
        1. 상품 이름
        2. 상품 개수
        3. 개당 가격

        <주문 관련 정보>
        1. + 상품 총 금액
        2. - 쿠폰으로 할인 받은 금액
        3. - 포인트로 할인 받은 금액
        4. - 쌓인 포인트 금액
        5. - 배송비
        6. - 반품비
        7. (계산) 환불 예상 금액
        8. 환불 수단 ("포인트")
*/

@Controller
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @GetMapping("client/order/{orderId}/payment/{paymentId}/refund")
    public String saveRefund(@PathVariable long orderId, @PathVariable long paymentId,
        Model model) {
        RefundOrderRequestDto refundOrderRequestDto = refundService.findRefundOrderRequestDtoByOrderId(
            orderId);
        model.addAttribute("refundOrderRequestDto", refundOrderRequestDto);
        return "/view/refund/refund";
    }
}