package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.temp.Coupon;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 사용자에게 결제와 관련된 정보를 보여줍니다.
    @GetMapping("/payment")
    public String createPayment(Model model) {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(1L, "testCoupon1"));
        coupons.add(new Coupon(2L, "testCoupon2"));
        model.addAttribute("coupons", coupons);
        model.addAttribute("remainingPoint", 10000);
        model.addAttribute("originalAmount", 20000);
        model.addAttribute("finalAmount", 18000);
        model.addAttribute("expectedPoints", 1800);
        return "/view/payment/createPayment";
    }

    // 사용자에게 결제와 관련된 정보를 입력 받습니다.
    @PostMapping("/payment")
    public void createPayment(@ModelAttribute PaymentRequestDto paymentRequestDto) {
        paymentService.createPayment(paymentRequestDto);
    }

    // 결제 정보를 단일로 조회할 수 있습니다.
    @GetMapping("payment/{paymentId}")
    public ResponseEntity<PaymentResponseDto> payment(@PathVariable("paymentId") Long paymentId) {
        ResponseEntity<PaymentResponseDto> paymentResponseDtoResponseEntity = paymentService.findPaymentByPaymentId(paymentId);
        return paymentResponseDtoResponseEntity;
    }
}
