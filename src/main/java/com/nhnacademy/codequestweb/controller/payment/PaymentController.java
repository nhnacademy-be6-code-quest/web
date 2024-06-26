package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.response.payment.OrderPaymentResponseDto;
import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final CouponService couponService;
    private final OrderService orderService; // ResponseDto -> 주문 총 금액
//    private final PointService pointService;
//    private final ClientService clientService;

    // 사용자에게 결제와 관련된 정보를 보여줍니다.
    @GetMapping("/client/order/payment")
    public String createPayment(@ModelAttribute OrderPaymentResponseDto orderPaymentResponseDto, Model model, HttpServletRequest httpServletRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(httpServletRequest, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(httpServletRequest, "refresh"));
        List<CouponResponseDto> couponList = couponService.findClientCoupon(headers);

//        String email = httpServletRequest.getHeader("email");
//        Long clientId = clientService.findClientIdByEmail(email);

        Long clientId = 1L;
        List<CouponResponseDto> coupons = couponService.findClientCoupon(headers);
        model.addAttribute("coupons", coupons);

        // 포인트에서 받아 올 것
        Long remainingPoint = 10000L;
//        Long remainingPoint = pointService.findByClientId(clientId);
        model.addAttribute("remainingPoint", remainingPoint);

        // 주문에서 받아 올 것
        Long orderId = 1L;
//        ResponseEntity<OrderPaymentResponseDto> orderPaymentResponseDtoResponseEntity = orderService.findOrderPaymentResponseDtoByOrderId(orderId);
        HttpHeaders orderHeaders = new HttpHeaders();
        orderHeaders.set("Content-Type", "application/json");
        HttpStatus httpStatus = HttpStatus.OK;
        orderPaymentResponseDto = new OrderPaymentResponseDto(1L, 20000L);
        model.addAttribute("orderPaymentResponseDto", orderPaymentResponseDto);
        log.error("orderPaymentResponseDto: {}", orderPaymentResponseDto);

        // 포인트 정책하고 같이 생각할 것. (일단 10퍼센트로 생각)
        model.addAttribute("expectedPoints", 1800);
        return "/view/payment/createPayment";
    }

    // 사용자에게 결제와 관련된 정보를 입력 받습니다.
    @PostMapping("client/order/payment")
    public String createPayment(@ModelAttribute PaymentRequestDto paymentRequestDto) {
//        paymentRequestDto.setOrderId(1L); // hidden input 으로 가져 왔음.
        paymentService.createPayment(paymentRequestDto);
        return "redirect:/client/order/payment/tossPayment";
    }

    // test
    @GetMapping("/client/order/payment/tossPayment")
    public String tossPayment() {
        return "/view/payment/tossPayment";
    }

    // 결제 정보를 단일로 조회할 수 있습니다.
    @GetMapping("/client/order/payment/{paymentId}")
    @ResponseBody
    public String findPaymentByPaymentId(@PathVariable Long paymentId, Model model) {
        ResponseEntity<PaymentResponseDto> paymentResponseDtoResponseEntity = paymentService.findByPaymentId(paymentId);
        model.addAttribute("paymentResponseDtoResponseEntity", paymentResponseDtoResponseEntity);
        return paymentResponseDtoResponseEntity.getBody().toString();
    }
}
