package com.nhnacademy.codequestweb.client.order;

import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/order/{orderId}/payment-request")
    ResponseEntity<PaymentOrderShowRequestDto> getPaymentOrderShowRequestDto(@RequestHeader HttpHeaders headers, @PathVariable Long orderId);

    @GetMapping("/{orderId}/approve-request")
    ResponseEntity<PaymentOrderApproveRequestDto> getPaymentOrderApproveRequestDto(@RequestHeader HttpHeaders headers, @PathVariable Long orderId);
}