package com.nhnacademy.codequestweb.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="TestClient", url="localhost:8003")
public interface TestClient {

    @GetMapping("/api/client/coupon-payment")
    ResponseEntity<Page<ClientCouponPaymentResponseDto>> getCouponClient(@RequestParam int page, @RequestParam int size);
}
