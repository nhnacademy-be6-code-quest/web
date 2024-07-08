package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.ProductOrderDetailRequestDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "paymentProduct", url = "http://localhost:8001")
public interface PaymentProductClient {

    @GetMapping("/")
    ResponseEntity<String> reduceInventory(List<ProductOrderDetailRequestDto> productOrderDetailRequestDtoList);
}