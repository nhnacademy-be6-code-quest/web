package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentClient", url = "http://localhost:8001/api/client/order/payment")
public interface PaymentClient {

    @PostMapping
    void savePayment(@RequestBody PaymentRequestDto paymentRequestDto);

    @GetMapping("/{paymentId}")
    ResponseEntity<PaymentResponseDto> findByPaymentId(@PathVariable Long paymentId);

    @GetMapping("/api/client/order/{orderId}/payment")
    long findTotalPriceByOrderId(@PathVariable Long orderId);
}