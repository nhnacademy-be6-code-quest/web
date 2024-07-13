package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.CouponPaymentRewardRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentCompletedCouponRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "paymentCoupon", url = "http://localhost:8001")
public interface PaymentCouponClient {

    @PutMapping("/api/coupon/payment")
    ResponseEntity<String> paymentUsedCoupon(
        @RequestHeader HttpHeaders headers,
        PaymentCompletedCouponRequestDto paymentCompletedCouponRequestDto);

    @PostMapping("/api/coupon/payment/reward")
    ResponseEntity<String> getUserPaymentValue(
        @RequestBody CouponPaymentRewardRequestDto couponPaymentRewardRequestDto);
}
