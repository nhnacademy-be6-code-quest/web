package com.nhnacademy.codequestweb.client.refund;

import com.nhnacademy.codequestweb.response.refund.PaymentRefundResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "refundOrderClient", url = "http://localhost:8001")
public interface RefundOrderClient {


    @GetMapping("/api/refund")
    ResponseEntity<PaymentRefundResponseDto> findPaymentKey(@RequestParam long orderId);
}
