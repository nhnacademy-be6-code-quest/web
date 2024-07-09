package com.nhnacademy.codequestweb.controller.refund;

import com.nhnacademy.codequestweb.service.refund.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

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
@Slf4j
public class RefundController {

    private final RefundService refundService;

    // 1. 환불 및 취소 사유 선택 받기
    @GetMapping("client/order/{orderId}/payment/{paymentId}/refund")
    public String saveRefund(@PathVariable long orderId, @PathVariable long paymentId,
        Model model) {

        // TODO 1) 주문의 상태를 가져 온다.
//        String orderStatus = refundService.getOrderStatus(orderId);
        String orderStatus = "결제대기";

        // TODO 2) 주문의 상태가 반품에 속하는지, 취소에 속하는지에 따라 정책을 가져 온다. (반품 및 취소 정책 타입에 들어 감.)
        List<RefundPolicyRequestDto> refundPolicyRequestDtoList = refundService.findAllRefundPolicyRequestDtoList();
        log.info("refundPolicyRequestDtoList: {}", refundPolicyRequestDtoList);
        model.addAttribute("refundPolicyRequestDtoList", refundPolicyRequestDtoList);


        // TODO [일단 나중에 하기]
        // TODO 3) 상품과 관련해서 돌려 받을 수 있는 금액을 보여 준다.
        // TODO 4) 후처리를 한다.

        model.addAttribute("orderId", orderId);
        model.addAttribute("paymentId", paymentId);
        model.addAttribute("orderStatus", orderStatus);
        return "view/refund/refund-reason";
    }

    // 2. 상품 관련해서 돌려 받을 환불 안내하기
//    @GetMapping("client/order/{orderId}/payment/{paymentId}/refund")
//    public String ddong(@PathVariable long orderId, @PathVariable long paymentId,
//        Model model) {
//        RefundOrderRequestDto refundOrderRequestDto = refundService.findRefundOrderRequestDtoByOrderId(
//            orderId);
//        model.addAttribute("refundOrderRequestDto", refundOrderRequestDto);
//        return "/view/refund/refund";
//    }

    // 3. 후처리하기
}