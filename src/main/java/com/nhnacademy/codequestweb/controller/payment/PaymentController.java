package com.nhnacademy.codequestweb.controller.payment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.payment.approve.PaymentApproveResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.payment.pg.PGServiceProvider;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    // PaymentController 의 Service 는 PaymentService 만 있어야 함. 단일 책임의 원칙...
    // 하나의 컨트롤러가 여러 개의 서비스를 호출하면 1) 테스트 어렵고 2) 확장성도 떨어짐

    private static final TypeReference<List<CartRequestDto>> TYPE_REFERENCE = new TypeReference<List<CartRequestDto>>() {};

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @GetMapping("/order/{orderCode}/payment/{pgName}/success")
    public String paymentResult(HttpServletRequest request, HttpServletResponse response, Model model,
        @PathVariable(value = "orderCode") String orderCode,
        @PathVariable(value = "pgName") String pgName) {

        String accessStr = CookieUtils.getCookieValue(request, "access");

        boolean isClient = accessStr != null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", accessStr);

        StringBuilder alterMessage = new StringBuilder();

        // TODO 예외처리. 결제실패 페이지로 보내기
        log.info("결제 승인 시도");
        PaymentApproveResponseDto approveResponseDto = paymentService.approvePayment(headers, orderCode, pgName, request.getParameterMap());
        log.info("결제승인 성공");

        if(isClient){
            log.info("결제 동기적 후처리 시도 - 회원 포인트 적립");
            boolean successAccumulatePoint = accumulatePoint(headers, approveResponseDto.getSuccessPaymentOrderInfo().getTotalPayAmount());
            if (!successAccumulatePoint) {
                alterMessage.append("포인트 적립에 실패했습니다. 관리자에게 문의하세요.");
            }
        }
        else {
            boolean successClearCartCookie = clearCartCookie(request, response, approveResponseDto.getSuccessPaymentOrderInfo().getProductIdList());
            if (!successClearCartCookie) {
                alterMessage.append("장바구니 쿠키 삭제에 실패했습니다");
            }
        }

        if(!alterMessage.isEmpty()) {
            model.addAttribute("alterMessage", alterMessage.toString());
        }

        model.addAttribute("successPaymentOrderInfo", approveResponseDto.getSuccessPaymentOrderInfo());
        model.addAttribute("view", "payment");
        model.addAttribute("payment", "success");

        return "index";
    }

    @GetMapping("/client/order/{orderCode}/payment/fail")
    public String paymentResult(
        @PathVariable String orderCode, Model model, @RequestParam(value = "message") String message,
        @RequestParam(value = "code") Integer code) {
        // orderCode로 재결제 유도하면 좋을듯!!
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        model.addAttribute("view", "payment");
        model.addAttribute("payment", "failed");
        return "index";
    }

    private boolean accumulatePoint(HttpHeaders headers, Long totalPayAmount) {
        try {
            paymentService.accumulatePoint(headers, totalPayAmount);
            return true;
        } catch (Exception e) {
            log.error("포인트 적립 처리 중 포인트 서버에서 에러가 발생했습니다.");
            return false;
        }
    }

    private boolean clearCartCookie(HttpServletRequest request, HttpServletResponse response,
        List<Long> productIdList) {

        String encodedCart = CookieUtils.getCookieValue(request, "cart");
        if (encodedCart != null) {
            try {
                String cartJson = new String(Base64.getDecoder().decode(encodedCart.getBytes()));
                List<CartRequestDto> cartListOfCookie = objectMapper.readValue(cartJson, TYPE_REFERENCE);
                List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

                for (CartRequestDto cartItem : cartListOfCookie) {
                    if (productIdList.contains(cartItem.productId())) {
                        cartRequestDtoToDelete.add(cartItem);
                    }
                }
                cartListOfCookie.removeAll(cartRequestDtoToDelete);

                CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, response);
                return true;
            } catch (Exception e) {
                return false;
            }
        }else{
            log.warn("cart controller advice may have some problem with processing deleted cookie. check the log with cart controller advice class");
            return false;
        }
    }

}