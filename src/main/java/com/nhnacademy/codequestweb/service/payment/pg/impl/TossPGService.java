package com.nhnacademy.codequestweb.service.payment.pg.impl;

import com.nhnacademy.codequestweb.request.payment.PaymentViewRequestDto;
import com.nhnacademy.codequestweb.request.payment.TossPaymentViewRequestDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.service.payment.pg.PGService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service("toss")
@RequiredArgsConstructor
public class TossPGService implements PGService {

    private final PaymentService paymentService;

    @Override
    public String getPaymentViewPath() {
        return "view/payment/tossPage";
    }

    @Override
    public void setPaymentViewModel(HttpHeaders headers, String orderCode, String pgName, Model model) {
        PaymentViewRequestDto paymentViewRequestDto = paymentService.getPaymentViewRequest(headers, orderCode, pgName);
        if (paymentViewRequestDto instanceof TossPaymentViewRequestDto tossPaymentViewRequestDto) {
            model.addAttribute("amount", tossPaymentViewRequestDto.getAmount());
            model.addAttribute("orderId", tossPaymentViewRequestDto.getOrderCode());
            model.addAttribute("orderName", tossPaymentViewRequestDto.getOrderName());
            model.addAttribute("successUrl", getSuccessUrl(orderCode));
            model.addAttribute("failUrl", getFailUrl(orderCode));
        }
    }

    private String getSuccessUrl(String orderCode) {
        return String.format("https://localhost:8080/client/order/%s/payment/toss/success", orderCode);
    }

    private String getFailUrl(String orderCode) {
        return String.format("https://localhost:8080/client/order/%s/payment/toss/fail", orderCode);
    }
}
