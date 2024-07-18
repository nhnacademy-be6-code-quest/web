package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.*;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    // PaymentController 의 Service 는 PaymentService 만 있어야 함. 단일 책임의 원칙...
    // 하나의 컨트롤러가 여러 개의 서비스를 호출하면 1) 테스트 어렵고 2) 확장성도 떨어짐

    private final PaymentService paymentService;
    private final CartService cartService;

    @GetMapping("/client/order/payment")
    public String savePayment(@RequestHeader HttpHeaders headers, Model model,
        @RequestParam("tossOrderId") String tossOrderId, HttpServletRequest req) {

        headers.set("access", CookieUtils.getCookieValue(req, "access"));

//      1. 주문에서 받은 값을 토대로 사용자에게 보여 주기
        PaymentOrderShowRequestDto paymentOrderShowRequestDto = paymentService.findPaymentOrderShowRequestDtoByOrderId(
            headers, tossOrderId);
        model.addAttribute("paymentOrderShowRequestDto", paymentOrderShowRequestDto);

        long orderTotalAmount = paymentOrderShowRequestDto.getOrderTotalAmount();
        long discountAmountByPoint = paymentOrderShowRequestDto.getDiscountAmountByPoint();
        long discountAmountByCoupon = paymentOrderShowRequestDto.getDiscountAmountByCoupon();

//        if (orderTotalAmount - discountAmountByPoint - discountAmountByCoupon == 0) {
//            String.format("redirect:/client/order/%s/payment/success/post-process", tossOrderId);
//        }

        // TODO localhost:8080 -> book-store.shop으로 변경
        model.addAttribute("successUrl",
            "https://localhost:8080/client/order/" + tossOrderId + "/payment/success");
        model.addAttribute("failUrl",
            "https://localhost:8080/client/order/" + tossOrderId + "/payment/fail");

        return "view/payment/tossPage";
    }

    @GetMapping("/client/order/{tossOrderId}/payment/success")
    public String paymentResult(HttpServletRequest request, Model model,
        @PathVariable(value = "tossOrderId") String tossOrderId,
        @RequestParam long amount, @RequestParam String paymentKey) throws ParseException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));

//        2. 결제 검증 및 승인 창에서 필요한 요소를 Order 에서 받아 오기 : @RequestHeader 로 해결함. // 303 ->
        // TODO redis에서 받아오기. 주문서비스 컨트롤러 추가해야함.
        PaymentOrderApproveRequestDto paymentOrderApproveRequestDto = paymentService.findPaymentOrderApproveRequestDtoByOrderId(
            headers, tossOrderId);

//        3. 조작 확인하기 : 주문 정보가 일치하지 않으면 실패 페이지로 이동하기.
        if (!paymentService.isValidTossPayment(paymentOrderApproveRequestDto, tossOrderId,
            amount)) {
            log.warn("주문 아이디 : {} 에서 결제 조작이 의심됩니다.", tossOrderId);
            model.addAttribute("alterMessage", "주문 아이디 : {} 에서 결제 조작이 의심됩니다.");
            return "view/payment/failed";
        }

        TossPaymentsResponseDto tossPaymentsResponseDto = paymentService.approvePayment(headers,
            tossOrderId, amount, paymentKey);

        // 7. DB에 저장하기
        paymentService.savePayment(headers, tossPaymentsResponseDto);

        // 9. View 보여주기
        //boolean isClient = paymentOrderApproveRequestDto.getClientId() != null;
        //model.addAttribute("isClient", isClient);
        model.addAttribute("isSuccess", true);
        model.addAttribute("tossOrderId", tossOrderId);
        //model.addAttribute("tossPaymentsResponseDto", tossPaymentsResponseDto);\

        return String.format("redirect:/client/order/%s/payment/success/post-process", tossOrderId);
    }

    @GetMapping("/client/order/{tossOrderId}/payment/success/post-process")
    public String postProcessPayment(@PathVariable("tossOrderId") String tossOrderId,
        HttpServletRequest request, HttpServletResponse response, Model model,
        RedirectAttributes redirectAttributes) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));

        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto = paymentService.getPostProcessRequiredPaymentResponseDto(
            tossOrderId);

        // 5-3) 포인트 적립하기
        boolean successAccumulatePoint = accumulatePoint(headers,
            postProcessRequiredPaymentResponseDto);

        // 포인트 적립 실패
        if (!successAccumulatePoint) {
            redirectAttributes.addFlashAttribute("alterMessage", "포인트 적립에 실패했습니다.");
            return "index";
        }

        // TODO 장바구니 비우기.
        boolean successClearCartCookie = clearCartCookie(response,
            postProcessRequiredPaymentResponseDto, model);

        if(!successClearCartCookie){
            redirectAttributes.addFlashAttribute("alterMessage", "장바구니 쿠기  적립에 실패했습니다.");
            return "index";
        }

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

    private boolean accumulatePoint(HttpHeaders headers,
        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto) {
        try {
            // 포인트 적립하기
            paymentService.accumulatePoint(headers,
                postProcessRequiredPaymentResponseDto.getAmount());
            return true;
        } catch (Exception e) {
            log.error("포인트 적립 처리 중 포인트 서버에서 에러가 발생했습니다.");
            return false;
        }
    }

    private boolean clearCartCookie(HttpServletResponse response,
        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto, Model model) {
        try {
            cartService.clearCartByCheckout(response,
                postProcessRequiredPaymentResponseDto.getProductIdList(), model);
            return true;
        } catch (Exception e) {
            log.error("장바구니 쿠키에서 구매한 상품들을 삭제하는 중에 에러가 발생했습니다.");
            return false;
        }
    }

}