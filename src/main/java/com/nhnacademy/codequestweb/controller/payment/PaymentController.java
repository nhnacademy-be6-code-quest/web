package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.request.payment.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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

        log.info("결제 요청");

        // 결제 요청 정보
        PaymentOrderShowRequestDto paymentOrderShowRequestDto = paymentService.findPaymentOrderShowRequestDtoByOrderId(
            headers, tossOrderId);
        model.addAttribute("paymentOrderShowRequestDto", paymentOrderShowRequestDto);

        log.info("결제 요청 정보: {}", paymentOrderShowRequestDto);

        // 쿠폰 및 포인트 할인 후 실 결제 금액이 0원일때?
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

        log.info("결제 요청 성공!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));

        // 결제 승인 요청 정보
        PaymentOrderApproveRequestDto paymentOrderApproveRequestDto = paymentService.findPaymentOrderApproveRequestDtoByOrderId(
            headers, tossOrderId);

        // 조작 확인하기 : 주문 정보가 일치하지 않으면 실패 페이지로 이동하기.
        if (!paymentService.isValidTossPayment(paymentOrderApproveRequestDto, tossOrderId,
            amount)) {
            log.warn("주문 아이디 : {} 에서 결제 조작이 의심됩니다.", tossOrderId);
            model.addAttribute("alterMessage", "주문 아이디 : {} 에서 결제 조작이 의심됩니다.");
            model.addAttribute("view", "payment");
            model.addAttribute("payment", "failed");
            return "index";
        }

        // 결제 승인하기
        TossPaymentsResponseDto tossPaymentsResponseDto = paymentService.approvePayment(headers,
            tossOrderId, amount, paymentKey);

        log.info("결제 승인 성공");

        // 결제 승인 후 DB에 저장
        paymentService.savePayment(headers, tossPaymentsResponseDto);

        log.info("결제 및 주문 데이터 저장 성공");

        return String.format("redirect:/client/order/%s/payment/success/post-process", tossOrderId);
    }

    @GetMapping("/client/order/{tossOrderId}/payment/success/post-process")
    public String postProcessPayment(@PathVariable("tossOrderId") String tossOrderId,

        HttpServletRequest request, HttpServletResponse response, Model model,
        RedirectAttributes redirectAttributes) {

        StringBuilder alterMessage = new StringBuilder();

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));

        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto = paymentService.getPostProcessRequiredPaymentResponseDto(
            tossOrderId);

        model.addAttribute("orderId", postProcessRequiredPaymentResponseDto.getOrderId());
        model.addAttribute("view", "payment");
        model.addAttribute("payment", "success");

        // 포인트 적립하기
        if(Objects.nonNull(postProcessRequiredPaymentResponseDto.getClientId())){

            boolean successAccumulatePoint = accumulatePoint(headers,
                postProcessRequiredPaymentResponseDto);

            // 포인트 적립 실패
            if (!successAccumulatePoint) {
                alterMessage.append("포인트 적립에 실패했습니다");
            }

        }

        // 장바구니 비우기
        boolean successClearCartCookie = clearCartCookie(response,
            postProcessRequiredPaymentResponseDto, model);

        if (!successClearCartCookie) {
            alterMessage.append("장바구니 쿠키 삭제에 실패했습니다");
        }

        if(!alterMessage.isEmpty()) {
            model.addAttribute("alterMessage", alterMessage.toString());
        }

        return "index";

    }

    @GetMapping("/client/order/{tossOrderId}/payment/fail")
    public String paymentResult(
        @PathVariable String tossOrderId, Model model, @RequestParam(value = "message") String message,
        @RequestParam(value = "code") Integer code) {
        // tossOrderId로 재결제 유도하면 좋을듯!!
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        model.addAttribute("view", "payment");
        model.addAttribute("payment", "failed");
        return "index";
    }

    private boolean accumulatePoint(HttpHeaders headers,
        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto) {
        try {
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