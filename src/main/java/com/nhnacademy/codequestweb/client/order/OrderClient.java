package com.nhnacademy.codequestweb.client.order;

import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 주문 클라이언트
 * @author 박희원(bakhuiwon326)
 **/

@FeignClient(name = "order", url = "http://localhost:8001")
public interface OrderClient {

    @PostMapping("/api/client/orders")
    ResponseEntity<Long> createClientOrder(@RequestHeader HttpHeaders headers, @RequestBody ClientOrderForm clientOrderForm);

    @PostMapping("/api/non-client/orders")
    ResponseEntity<Long> createNonClientOrder(@RequestHeader HttpHeaders headers, @RequestBody NonClientOrderForm nonClientOrderForm);

//    // 회원 주문
//    @PostMapping("/api/client/views/order")
//    ResponseEntity<ClientViewOrderPostResponseDto> viewOrder(ClientViewOrderPostRequestDto orderRequestDto);

//    @PostMapping("/api/client/order")
//    ResponseEntity<ClientOrderPostResponseDto> createOrder(ClientOrderPostRequestDto clientOrderPostRequestDto);

}