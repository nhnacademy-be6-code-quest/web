package com.nhnacademy.codequestweb.client.refund;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "refundOrderClient", url = "http://localhost:8001")
public interface RefundOrderClient {

    // TODO : API 구현 필요합니다.
    @GetMapping("/api/order/refund-request")
    public String findOrderStatusByOrderId(Long orderId);
}
