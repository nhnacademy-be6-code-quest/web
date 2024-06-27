package com.nhnacademy.codequestweb.client.order;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "orderReviewClient", url = "http://localhost:8008")
public interface OrderReviewClient {

    @GetMapping("/order-status/{orderDetailId}")
    ResponseEntity<String> getOrderStatus(@PathVariable long orderDetailId);

}
