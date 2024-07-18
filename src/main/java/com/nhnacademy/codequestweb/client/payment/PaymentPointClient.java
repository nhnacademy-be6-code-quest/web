package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import com.nhnacademy.codequestweb.request.point.PointRewardOrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "paymentPoint", url = "http://localhost:8001")
public interface PaymentPointClient {

    @PostMapping("/api/point/use/payment")
    ResponseEntity<String> usePaymentPoint(
        @RequestBody PaymentUsePointRequestDto paymentUsePointRequestDto,
        @RequestHeader HttpHeaders headers);

    @PostMapping("/api/point/order")
    ResponseEntity<String> rewardOrderPoint(@RequestHeader HttpHeaders headers,
        @RequestBody PointRewardOrderRequestDto pointRewardOrderRequestDto);
}
