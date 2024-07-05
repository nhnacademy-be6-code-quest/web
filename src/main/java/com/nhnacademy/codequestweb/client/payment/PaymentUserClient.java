package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "paymentUser", url = "http://localhost:8001")
public interface PaymentUserClient {

    @GetMapping("/api/client")
    ResponseEntity<ClientPrivacyResponseDto> getPrivacy(@RequestHeader HttpHeaders headers);
}