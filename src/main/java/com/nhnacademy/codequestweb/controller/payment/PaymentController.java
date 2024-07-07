package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentAccumulatePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto2;
import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        // TODO 1. 주문에서 받은 값을 토대로 사용자에게 보여 주기
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
        TODO 2. 사용자의 조작을 검증하기 위한 요소 Order 에서 받아 오기
        PaymentOrderValidationRequestDto paymentOrderValidationRequestDto = paymentService.findPaymentOrderValidationDtoByOrderId(orderId);
        PaymentOrderValidationRequestDto paymentOrderValidationRequestDto = PaymentOrderValidationRequestDto.builder()
            .orderTotalAmount(100000L)
            .discountAmountByCoupon(20000L)
            .discountAmountByPoint(20000L)
            .tossOrderId("287af443-771e-4b04-9428-8031d72cff25") // 서버에 저장하고 있어야 하는 값
            .build();

//        TODO 3. 조작 확인하기. 조작 의심 시 승인하지 않고 실패 View 로 보냄.
        if (!paymentService.isValidTossPayment(paymentOrderValidationRequestDto, tossOrderId,
            amount)) {
            // 주문 정보가 일치하지 않으면 에러 처리
            model.addAttribute("isSuccess", false);
            model.addAttribute("code", "INVALID_ORDER");
            model.addAttribute("message", "주문 정보가 일치하지 않습니다.");
            log.info("Check payment history");
            return "view/payment/failed";
        }
        TODO 지우면 안 됨!!! 테스트 간결하게 하기 위해 잠시 주석 처리 함.
         */

//      TODO 4. 토스 페이먼트에게 결제를 승인. 실제로 결제를 진행함.

        // 1) 토스에게 결제를 승인 받고 응답을 받아 오며, 사용자에게 보여주기 위해 Response Dto 형태로 파싱 함.
        TossPaymentsResponseDto tossPaymentsResponseDto = paymentService.approvePayment(tossOrderId,
            amount, paymentKey);

        // clientId, couponId, discountAmountByPoint, accumulatedPoint, 재고
        PaymentOrderRequestDto2 paymentOrderRequestDto2 = paymentService.findPaymentOrderRequestDto2ByOrderId(
            orderId);

        // TODO : 응답의 형태에 따라 쿠폰, 포인트, 적립, 재고 등의 이벤트가 적절하게 발생했는지 판단하고, 사용자에게 보여줄 것은 보여주고, 시스템에 에러로 남길 것은 남겨 두기
        // 2) 쿠폰 사용 처리
        paymentService.useCoupon(paymentOrderRequestDto2.getCouponId());

        // 3) 포인트 사용 처리
        paymentService.usePoint(PaymentUsePointRequestDto.builder()
            .clientId(paymentOrderRequestDto2.getClientId())
            .discountAmountByPoint(paymentOrderRequestDto2.getDiscountAmountByPoint())
            .build()
        );

        // 4) 포인트 적립 처리
        paymentService.accumulatePoint(PaymentAccumulatePointRequestDto.builder()
            .clientId(paymentOrderRequestDto2.getClientId())
            .accumulatedPoint(paymentOrderRequestDto2.getAccumulatedPoint())
            .build()
        );

        // 5) 재고 감소 처리
        Map<Long, Long> reduceInventoryMap = paymentService.processReduceInventoryMap(
            paymentOrderRequestDto2);
        paymentService.reduceInventory(reduceInventoryMap);

        // 결제 성공 페이지로 이동
        paymentService.savePayment(orderId, tossPaymentsResponseDto);
        model.addAttribute("tossPaymentsResponseDto", tossPaymentsResponseDto);
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