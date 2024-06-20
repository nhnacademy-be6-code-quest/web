package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "paymentClient", url = "http://localhost:8008/payment")
public interface PaymentClient {

    @PostMapping
    void createPayment(PaymentRequestDto paymentRequestDto);

    @GetMapping("/{paymentId}")
    ResponseEntity<PaymentResponseDto> findPaymentByPaymentId(@PathVariable Long paymentId);
}