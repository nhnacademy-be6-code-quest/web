package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentCompletedCouponRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "paymentCoupon", url = "http://localhost:8001")
public interface PaymentCouponClient {

    @PutMapping("/api/coupon/payment")
    ResponseEntity<String> paymentUsedCoupon(
        PaymentCompletedCouponRequestDto paymentCompletedCouponRequestDto);
}
