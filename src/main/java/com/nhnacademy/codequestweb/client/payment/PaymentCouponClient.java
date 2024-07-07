package com.nhnacademy.codequestweb.client.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "paymentCoupon", url = "http://localhost:8001")
public interface PaymentCouponClient {

    @GetMapping("/")
    void useCoupon(long couponId);
}
