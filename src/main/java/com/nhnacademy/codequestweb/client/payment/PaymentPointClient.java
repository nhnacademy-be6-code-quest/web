package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentAccumulatePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "paymentPoint", url = "http://localhost:8001")
public interface PaymentPointClient {

    @GetMapping("/")
    void usePoint(PaymentUsePointRequestDto paymentUsePointRequestDto);

    @GetMapping("/")
    void accumulatePoint(PaymentAccumulatePointRequestDto paymentAccumulatePointRequestDto);
}
