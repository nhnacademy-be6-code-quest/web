package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentClient", url = "http://localhost:8001/api/client/order")
public interface PaymentClient {

    @PostMapping("{orderId}/payment")
    void savePayment(@PathVariable long orderId, @RequestBody TossPaymentsResponseDto tossPaymentsResponseDto);
}