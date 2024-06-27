package com.nhnacademy.codequestweb.client.order;

import com.nhnacademy.codequestweb.request.order.ClientViewOrderPostRequestDto;
import com.nhnacademy.codequestweb.request.order.client.ClientOrderPostRequestDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderPostResponseDto;
import com.nhnacademy.codequestweb.response.order.client.ClientViewOrderPostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order", url = "http://localhost:8001")
public interface OrderClient {

    @GetMapping("/api/client/views/order")
    ResponseEntity<ClientViewOrderPostResponseDto> viewOrder(ClientViewOrderPostRequestDto orderRequestDto);

    @PostMapping("/api/client/order")
    ResponseEntity<ClientOrderPostResponseDto> createOrder(ClientOrderPostRequestDto clientOrderPostRequestDto);

}