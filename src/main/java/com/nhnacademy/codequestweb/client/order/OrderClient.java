package com.nhnacademy.codequestweb.client.order;

import com.nhnacademy.codequestweb.request.order.ClientOrderPostRequestDto;
import com.nhnacademy.codequestweb.response.order.ClientOrderPostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order", url = "http://localhost:8008")
public interface OrderClient {
    @PostMapping("/client/order")
    ResponseEntity<ClientOrderPostResponseDto> tryOrder(@RequestBody ClientOrderPostRequestDto orderRequestDto);
}