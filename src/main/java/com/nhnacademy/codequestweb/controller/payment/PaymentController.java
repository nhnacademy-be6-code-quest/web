package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    // PaymentController 의 Service 는 PaymentService 만 있어야 함. 단일 책임의 원칙...
    // 하나의 컨트롤러가 여러 개의 서비스를 호출하면 1) 테스트 어렵고 2) 확장성도 떨어짐
    private final PaymentService paymentService;

    @GetMapping("/client/order/{orderId}/payment")
    public String savePayment(@PathVariable long orderId, Model model) {

        List<String> productNameList = new ArrayList<>();
        productNameList.add("10만 원짜리 테스트 상품");
        productNameList.add("20만 원짜리 테스트 상품");
        productNameList.add("30만 원짜리 테스트 상품");
        // TODO : 주문에서 이것만 잘 받으면 됨.
//        PaymentOrderRequestDto paymentOrderRequestDto = paymentService.findPaymentOrderRequestDtoByOrderId(orderId);
        PaymentOrderRequestDto paymentOrderRequestDto = PaymentOrderRequestDto.builder()
            .orderTotalAmount(1000000L)
            .discountAmountByCoupon(20000L)
            .discountAmountByPoint(20000L)
            .tossOrderId(UUID.randomUUID().toString())
            .productNameList(productNameList)
            .build();
        model.addAttribute("paymentOrderRequestDto", paymentOrderRequestDto);
        model.addAttribute("successUrl",
            "https://localhost:8080/client/order/" + orderId + "/payment/success");
        model.addAttribute("failUrl",
            "https://localhost:8080/client/order/" + orderId + "/payment/fail");
        return "/view/payment/tossPage";
    }

    @GetMapping("/client/order/{orderId}/payment/success")
    public String paymentResult(
        @PathVariable long orderId, Model model,
        @RequestParam(value = "orderId") String tossOrderId,
        @RequestParam long amount, @RequestParam String paymentKey) throws ParseException {

        /*
        TODO 지우면 안 됨!!! 테스트 간결하게 하기 위해 잠시 주석 처리 함.
        PaymentOrderValidationRequestDto paymentOrderValidationRequestDto = paymentService.findPaymentOrderValidationDtoByOrderId(orderId);
        PaymentOrderValidationRequestDto paymentOrderValidationRequestDto = PaymentOrderValidationRequestDto.builder()
            .orderTotalAmount(100000L)
            .discountAmountByCoupon(20000L)
            .discountAmountByPoint(20000L)
            .tossOrderId("287af443-771e-4b04-9428-8031d72cff25") // 서버에 저장하고 있어야 하는 값
            .build();

//        TODO 4. 사용자에게 결제 수단을 잘 받아 왔다면, tossOrderId, amount 가 조작되지는 않았는지 확인함.
        if (!paymentService.isValidTossPayment(paymentOrderValidationRequestDto, tossOrderId,
            amount)) {
            // 주문 정보가 일치하지 않으면 에러 처리
            model.addAttribute("isSuccess", false);
            model.addAttribute("code", "INVALID_ORDER");
            model.addAttribute("message", "주문 정보가 일치하지 않습니다.");
            return "view/payment/failed";
        }
        TODO 지우면 안 됨!!! 테스트 간결하게 하기 위해 잠시 주석 처리 함.
         */

//         TODO 5. 토스 페이먼트에게 결제를 승인 받음. // @NotNull 애너테이션이 있어서 -> Validation 가능
        JSONObject jsonObject = paymentService.approvePayment(tossOrderId,
            amount, paymentKey);
        TossPaymentsResponseDto tossPaymentsResponseDto = paymentService.parseJSONObject(
            jsonObject);
        // 결제 성공 페이지로 이동
        paymentService.savePayment(orderId, tossPaymentsResponseDto);
        model.addAttribute("tossPaymentsResponseDto", tossPaymentsResponseDto);
        model.addAttribute("jsonObject", jsonObject); // 딱히 필요 없을 듯?
        return "view/payment/success";
    }

    @GetMapping("/client/order/{orderId}/payment/fail")
    public String paymentResult(
        @PathVariable long orderId, Model model, @RequestParam(value = "message") String message,
        @RequestParam(value = "code") Integer code) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "view/payment/failed";
    }
}