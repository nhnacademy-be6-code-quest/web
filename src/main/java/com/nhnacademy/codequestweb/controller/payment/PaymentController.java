package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.response.payment.OrderPaymentResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    /*
        TODO: 주문에서 결제로 넘어와야 하는 값
            - 구매자의 고유 아이디
            - 회원 주문인지, 비회원 주문인지 식별할 수 있는 값
            - 최종적으로 결제해야 하는 값 (order_total_amount, discount_amount_by_coupon, discount_amount_by_point)
            - 결제한 상품 이름 1개 및 총 주문 개수 ex) "NHN Academy 에서 살아남기 외 2권"
            - successUrl, failUrl
            - 결제자 이메일
            - 결제자 이름
            - 결제자 휴대폰 번호 (주문자 휴대폰 번호가 아님)
     */

    @GetMapping("/client/order/payment")
    public String savePayment(
        // TODO: 나중에 주석 풀기 @PathVariable long orderId,
        Model model,
        @ModelAttribute OrderPaymentResponseDto orderPaymentResponseDto) {

        long payAmount = 99999L;
        model.addAttribute("payAmount", payAmount);

        String tossOrderId = UUID.randomUUID().toString();
        model.addAttribute("tossOrderId", tossOrderId);

        String orderName = "초코파이 외 10건";
        model.addAttribute("orderName", orderName);

        String customerName = "김채호";
        model.addAttribute("customerName", customerName);

        String successUrl = "https://localhost:8080/client/order/payment/success";
        model.addAttribute("successUrl", successUrl);

        String failUrl = "https://localhost:8080/client/order/payment/fail";
        model.addAttribute("failUrl", failUrl);

        return "/view/payment/tossPage";
    }

    @GetMapping("/client/order/payment/success")
    public String successPayment() {
        return "view/payment/success";
    }

    @GetMapping("/client/order/payment/fail")
    public String failedPayment() {
        return "view/payment/failed";
    }

//    // 사용자에게 결제와 관련된 정보를 입력 받습니다.
//    @PostMapping("client/order/payment")
//    public void savePayment(/* TODO: 나중에 주석 풀기 @PathVariable long orderId */) {
//    }
}