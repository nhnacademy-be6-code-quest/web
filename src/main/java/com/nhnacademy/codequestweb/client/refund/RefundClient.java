package com.nhnacademy.codequestweb.client.refund;

import com.nhnacademy.codequestweb.request.refund.PaymentCancelRequestDto;
import com.nhnacademy.codequestweb.response.refund.PaymentCancelResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "refundOrderClient", url = "http://localhost:8001")
public interface RefundClient {


    @GetMapping("/api/refund")
    ResponseEntity<PaymentCancelResponseDto> findPaymentKey(@RequestParam long orderId);

    @PostMapping("/api/refund/cancel")
    void paymentCancel(@RequestHeader("access") String access, @RequestBody PaymentCancelRequestDto paymentCancelRequestDto);
}
