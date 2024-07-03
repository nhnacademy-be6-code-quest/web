package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.TossPaymentsRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tossPaymentsClient", url = "https://api.tosspayments.com/v1/payments/confirm")
public interface TossPaymentsClient {

    @PostMapping
    String approvePayment(@RequestBody TossPaymentsRequestDto tossPaymentsRequestDto,
        @RequestHeader String authorization,
        @RequestHeader String contentType);
}