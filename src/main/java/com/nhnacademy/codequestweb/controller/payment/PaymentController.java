package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.OrderPaymentRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final CouponService couponService;
    private final OrderService orderService; // ResponseDto -> 주문 총 금액
//    private final ClientService clientService;

    // 사용자에게 결제와 관련된 정보를 보여줍니다.
    @GetMapping("/client/order/{orderId}/payment")
    public String createPayment(@PathVariable(name = "orderId") long orderId, Model model, HttpServletRequest httpServletRequest) {
//        String email = httpServletRequest.getHeader("email");
//        Long clientId = clientService.findClientIdByEmail(email);x
        Long clientId = 1L;
        //List<CouponResponseDto> coupons = couponService.findClientCoupon(clientId);
        List<CouponResponseDto> coupons = new ArrayList<>();
        model.addAttribute("coupons", coupons);

        // 포인트에서 받아 올 것
        model.addAttribute("remainingPoint", 10000);

        // 주문에서 받아 올 것
        //Long orderId = 1L;
        //Long orderId = orderPaymentRequestDto.getOrderId();
        Long totalPrice = 100L;//orderPaymentRequestDto.getTotalPrice();

        model.addAttribute("totalPrice", totalPrice);

        // 주문에서 받아 올 것을 토대로
//        model.addAttribute("finalAmount", 18000);

        // 포인트 정책하고 같이 생각할 것.
        model.addAttribute("expectedPoints", 1800);
        return "view/payment/createPayment";
    }

    // 사용자에게 결제와 관련된 정보를 입력 받습니다.
    @PostMapping("/client/order/payment")
    public void createPayment(@ModelAttribute PaymentRequestDto paymentRequestDto) {
        paymentRequestDto.setOrderId(1L);
        paymentRequestDto.setClientDeliveryAddressId(1L);
        paymentService.createPayment(paymentRequestDto);
    }

    // 결제 정보를 단일로 조회할 수 있습니다.
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<PaymentResponseDto> payment(@PathVariable("paymentId") Long paymentId) {
        ResponseEntity<PaymentResponseDto> paymentResponseDtoResponseEntity = paymentService.findPaymentByPaymentId(paymentId);
        return paymentResponseDtoResponseEntity;
    }
}
