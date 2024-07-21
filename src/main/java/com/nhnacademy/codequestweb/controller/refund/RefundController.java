package com.nhnacademy.codequestweb.controller.refund;

import com.nhnacademy.codequestweb.request.refund.PaymentCancelRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundAfterRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundRequestDto;
import com.nhnacademy.codequestweb.response.refund.RefundAdminResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundPolicyResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundSuccessResponseDto;
import com.nhnacademy.codequestweb.service.refund.RefundService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/client/payment/cancel")
    public String paymentCancel(@ModelAttribute PaymentCancelRequestDto paymentCancelRequestDto,
                                RedirectAttributes redirectAttributes) {

        refundService.cancelRequest(paymentCancelRequestDto);
        redirectAttributes.addFlashAttribute("alterMessage", "취소가 완료되었습니다.");
        return "redirect:/mypage/orders";
    }

    @GetMapping("/order/refund")
    public ResponseEntity<List<RefundPolicyResponseDto>> viewRefundInfo(
            @RequestParam long orderId) {
        try {
            List<RefundPolicyResponseDto> result = refundService.findRefundPay(orderId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/order/refund/request")
    public String requestRefund(@ModelAttribute RefundRequestDto requestDto, RedirectAttributes redirectAttributes) {
        RefundSuccessResponseDto dto = refundService.requestRefund(requestDto);
        redirectAttributes.addFlashAttribute("alterMessage", "환불금액이" + dto.getRefundAmount() + "원 적립될 예정입니다.");
        return "redirect:/mypage/orders";
    }

    @GetMapping("/client/refund/view")
    public ResponseEntity<RefundAdminResponseDto> refundUser(@RequestParam long orderId) {
        return ResponseEntity.ok(refundService.userRefund(orderId));
    }

    @PostMapping("/client/refund/sure")
    public String refundSure(@ModelAttribute RefundAfterRequestDto refundAfterRequestDto, RedirectAttributes redirectAttributes) {
        refundService.refundSure(refundAfterRequestDto);
        redirectAttributes.addFlashAttribute("alterMessage", "환불 완료");
        return "redirect:/admin/orders";
    }
}