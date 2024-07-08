package com.nhnacademy.codequestweb.client.payment;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "paymentProduct", url = "http://localhost:8001")
public interface PaymentProductClient {

    @GetMapping("/")
    void reduceInventory(Map<Long, Long> reduceInventoryMap);
}
