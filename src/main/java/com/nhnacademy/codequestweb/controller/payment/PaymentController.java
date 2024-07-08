package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentCompletedCouponRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto2;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
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
        // TODO 1. 주문에서 받은 값을 토대로 사용자에게 보여 주기
        PaymentOrderRequestDto paymentOrderRequestDto = paymentService.findPaymentOrderRequestDtoByOrderId(
            orderId);
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

//         TODO 2. 결제 검증 및 승인 창에서 필요한 요소를 Order 에서 받아 오기
        PaymentOrderRequestDto2 paymentOrderRequestDto2 = paymentService.findPaymentOrderRequestDto2ByOrderId(
            orderId);

//         TODO 3. 조작 확인하기 : 주문 정보가 일치하지 않으면 실패 페이지로 이동하기.
        if (!paymentService.isValidTossPayment(paymentOrderRequestDto2, tossOrderId, amount)) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("code", "INVALID_ORDER");
            model.addAttribute("message", "주문 정보가 일치하지 않습니다.");
            log.info("Check payment history");
            return "view/payment/failed";
        }

//      TODO 4. 토스 페이먼트에게 결제를 승인. 실제로 결제를 진행함.
//       1) 토스에게 결제를 승인 받고 응답을 받아 오며, 사용자에게 보여주기 위해 Response Dto 형태로 파싱 함.
        TossPaymentsResponseDto tossPaymentsResponseDto = paymentService.approvePayment(tossOrderId,
            amount, paymentKey);

        // TODO : 응답의 형태에 따라 쿠폰, 포인트, 적립, 재고 등의 이벤트가 적절하게 발생했는지 판단하고, 사용자에게 보여줄 것은 보여주고, 시스템에 에러로 남길 것은 남겨 두기
        // 2) 쿠폰 사용 처리
        boolean couponResponse = paymentService.useCoupon(
            PaymentCompletedCouponRequestDto.builder()
                .couponId(paymentOrderRequestDto2.getCouponId())
                .build()
        ).getStatusCode().is2xxSuccessful();
        model.addAttribute("couponResponse", couponResponse);

        if (!couponResponse) {
            log.error("쿠폰 사용 처리에 실패했습니다.");
            log.error("쿠폰 아이디: {}", paymentOrderRequestDto2.getCouponId());
        }

//        // 3) 포인트 사용 처리
//        paymentService.usePoint(PaymentUsePointRequestDto.builder()
//            .clientId(paymentOrderRequestDto2.getClientId())
//            .discountAmountByPoint(paymentOrderRequestDto2.getDiscountAmountByPoint())
//            .build()
//        );
//
//        // 4) 포인트 적립 처리
//        paymentService.accumulatePoint(PaymentAccumulatePointRequestDto.builder()
//            .clientId(paymentOrderRequestDto2.getClientId())
//            .accumulatedPoint(paymentOrderRequestDto2.getAccumulatedPoint())
//            .build()
//        );

        // 5) 재고 감소 처리 : TODO : [return type -> ResponseEntity<>()]
//        boolean productResponse = paymentService.reduceInventory(
//            paymentOrderRequestDto2.getProductOrderDetailList()).getStatusCode().is2xxSuccessful();

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