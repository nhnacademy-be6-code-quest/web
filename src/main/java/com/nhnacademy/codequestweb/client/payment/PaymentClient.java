package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.ApprovePaymentRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentMethodResponseDto;
import com.nhnacademy.codequestweb.response.payment.approve.PaymentApproveResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderPaymentRefund API 로 결제 정보를 저장하는 FeignClient 파일입니다. 주문 ID와 Toss Payments 결제 응답 정보를 전달받아
 * 서버에 저장합니다.
 *
 * @author 김채호
 * @version 1.0
 */
@FeignClient(name = "paymentClient", url = "http://localhost:8001")
public interface PaymentClient {

    @GetMapping("/api/payment/grade/{clientId}")
    ResponseEntity<PaymentGradeResponseDto> getPaymentRecordOfClient(@PathVariable Long clientId);

    @PostMapping("/api/order/payment/approve")
    ResponseEntity<PaymentApproveResponseDto> approvePayment(@RequestHeader HttpHeaders headers, @RequestBody ApprovePaymentRequestDto approvePaymentRequestDto);

    @GetMapping("/api/payment/method")
    ResponseEntity<List<PaymentMethodResponseDto>> getAllPaymentMethod(@RequestHeader HttpHeaders headers);
}