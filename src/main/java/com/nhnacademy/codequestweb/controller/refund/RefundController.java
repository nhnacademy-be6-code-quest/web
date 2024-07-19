package com.nhnacademy.codequestweb.controller.refund;

import com.nhnacademy.codequestweb.request.refund.RefundTossRequestDto;
import com.nhnacademy.codequestweb.response.refund.PaymentRefundResponseDto;
import com.nhnacademy.codequestweb.response.refund.TossPaymentRefundResponseDto;
import com.nhnacademy.codequestweb.service.refund.RefundService;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("client/order/{orderId}/refund")
    public String saveRefund(@PathVariable long orderId,
        Model model, RedirectAttributes redirectAttributes) throws ParseException {

        PaymentRefundResponseDto paymentRefundResponseDto = refundService.findTossKey(
            orderId);
        if(paymentRefundResponseDto.getOrderStatus().equals("결제완료")){
            RefundTossRequestDto dto = RefundTossRequestDto.builder()
                .cancelReason("결제 취소")
                .orderStatus(paymentRefundResponseDto.getOrderStatus())
                .paymentId(paymentRefundResponseDto.getPaymentId())
                .tossPaymentKey(paymentRefundResponseDto.getTossPaymentKey()).build();
           redirectAttributes.addFlashAttribute("alterMessage", "결재 취소");

           refundService.saveRefund(orderId, dto);
            return "redirect:/mypage/orders";

        }else{
            model.addAttribute("orderId", orderId);
            model.addAttribute("dto", paymentRefundResponseDto);
            return "view/refund/refund-reason";
        }




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