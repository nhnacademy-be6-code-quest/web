package com.nhnacademy.codequestweb.client.order;

import com.nhnacademy.codequestweb.request.order.OrderRequestDto;
import com.nhnacademy.codequestweb.response.order.OrderResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "orderClient", url = "http://localhost:8008")
public interface OrderClient {
    @PostMapping("/order")
    ResponseEntity<OrderResponseDto> tryOrder(@Valid @RequestBody OrderRequestDto orderRequestDto);
}