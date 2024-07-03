package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderValidationRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentClient", url = "http://localhost:8001/api/client/order")
public interface PaymentClient {

    @PostMapping("{orderId}/payment")
    void savePayment(@PathVariable long orderId, @RequestBody PaymentResponseDto paymentResponseDto);

    @GetMapping("{orderId}/payment/{paymentId}")
    ResponseEntity<PaymentResponseDto> findByPaymentId(@PathVariable Long paymentId,
        @PathVariable long orderId);

    @GetMapping("{orderId}")
    PaymentOrderRequestDto findOrderPaymentResponseDtoByOrderId(@PathVariable long orderId);

    @GetMapping("{orderId}")
    PaymentOrderValidationRequestDto findOrderTossPaymentResponseDtoByOrderId(@PathVariable long orderId);
}