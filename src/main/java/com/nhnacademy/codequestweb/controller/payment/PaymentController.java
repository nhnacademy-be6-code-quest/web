package com.nhnacademy.codequestweb.controller.payment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.request.payment.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.test.PaymentMethodProvider;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.SecretKeyUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
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
    private final ObjectMapper objectMapper;
    private static final TypeReference<List<CartRequestDto>> TYPE_REFERENCE = new TypeReference<List<CartRequestDto>>() {};
    private final CartService cartService;
    private final PaymentMethodProvider paymentMethodProvider;

    @GetMapping("/client/order/payment")
    public String savePayment(@RequestHeader HttpHeaders headers, Model model,
        @RequestParam("orderCode") String orderCode, HttpServletRequest req, @RequestParam("method") String name) {

        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        log.info("결제 요청");

        // 결제 요청 정보
        PaymentOrderShowRequestDto paymentOrderShowRequestDto = paymentService.findPaymentOrderShowRequestDtoByOrderId(
            headers, orderCode);
        model.addAttribute("paymentOrderShowRequestDto", paymentOrderShowRequestDto);

        log.info("결제 요청 정보: {}", paymentOrderShowRequestDto);

        // 쿠폰 및 포인트 할인 후 실 결제 금액이 0원일때?
        long amount= paymentOrderShowRequestDto.getOrderTotalAmount() - paymentOrderShowRequestDto.getDiscountAmountByPoint() - paymentOrderShowRequestDto.getDiscountAmountByCoupon();
        if (amount == 0) {
            log.error("결제1"+orderCode);
            return "redirect:/client/order/"+orderCode+"/payment/success?amount="+amount+"&paymentKey=point&method=point" ;

        }

        model.addAttribute("successUrl",
            "https://localhost:8080/client/order/" + orderCode + "/payment/success?method="+name);
        model.addAttribute("failUrl",
            "https://localhost:8080/client/order/" + orderCode + "/payment/fail");


        return paymentMethodProvider.getName(name);
    }

    @GetMapping("/client/order/{orderCode}/payment/success")
    public String paymentResult(HttpServletRequest request, Model model,
        @RequestParam("method") String name,
        @PathVariable(value = "orderCode") String orderCode,
        @RequestParam long amount, @RequestParam(required = false) String paymentKey, @RequestParam(required = false) String paymentId) throws ParseException {


        String paymentIdentifier = (paymentKey != null) ? paymentKey : paymentId;

        log.info("결제 요청 성공!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));

        // 결제 승인 요청 정보
        PaymentOrderApproveRequestDto paymentOrderApproveRequestDto = paymentService.findPaymentOrderApproveRequestDtoByOrderId(
            headers, orderCode);

        // 조작 확인하기 : 주문 정보가 일치하지 않으면 실패 페이지로 이동하기.
        if (!paymentService.isValidTossPayment(paymentOrderApproveRequestDto, orderCode,
            amount)) {
            log.warn("주문 아이디 : {} 에서 결제 조작이 의심됩니다.", orderCode);
            model.addAttribute("alterMessage", "주문 아이디 : {} 에서 결제 조작이 의심됩니다.");
            model.addAttribute("view", "payment");
            model.addAttribute("payment", "failed");
            return "index";
        }
        log.error("승인1"+orderCode);
        // 결제 승인하기
        PaymentsResponseDto paymentsResponseDto = paymentService.approvePayment(headers, name,
            orderCode, amount, paymentKey);

        log.info("결제 승인 성공");

        // 결제 승인 후 DB에 저장
        paymentService.savePayment(headers, paymentsResponseDto);

        log.info("결제 및 주문 데이터 저장 성공");
        log.error("승인2"+orderCode);
        return String.format("redirect:/client/order/%s/payment/success/post-process", orderCode);
    }

    @GetMapping("/client/order/{orderCode}/payment/success/post-process")
    public String postProcessPayment(@PathVariable("orderCode") String orderCode,

        HttpServletRequest request, HttpServletResponse response, Model model,
        RedirectAttributes redirectAttributes) {

        StringBuilder alterMessage = new StringBuilder();

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(request, "access"));
        log.error("후처리1"+orderCode);
        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto = paymentService.getPostProcessRequiredPaymentResponseDto(headers, orderCode);

        model.addAttribute("orderId", postProcessRequiredPaymentResponseDto.getOrderId());
        model.addAttribute("view", "payment");
        model.addAttribute("payment", "success");
        log.error("후처리2"+orderCode);
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
        boolean successClearCartCookie = clearCartCookie(request, response,
            postProcessRequiredPaymentResponseDto, model);

        if (!successClearCartCookie) {
            alterMessage.append("장바구니 쿠키 삭제에 실패했습니다");
        }

        if(!alterMessage.isEmpty()) {
            model.addAttribute("alterMessage", alterMessage.toString());
        }

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

    private boolean clearCartCookie(HttpServletRequest request, HttpServletResponse response,
        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto, Model model) {

        String encryptedCart = CookieUtils.getCookieValue(request, "cart");
        if (encryptedCart != null) {
            try {
                String cartJson = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
                List<CartRequestDto> cartListOfCookie = objectMapper.readValue(cartJson, TYPE_REFERENCE);
                List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

                for (CartRequestDto cartItem : cartListOfCookie) {
                    if (postProcessRequiredPaymentResponseDto.getProductIdList().contains(cartItem.productId())) {
                        cartRequestDtoToDelete.add(cartItem);
                    }
                }
                cartListOfCookie.removeAll(cartRequestDtoToDelete);

                CookieUtils.deleteCookieValue(response, "cart");
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