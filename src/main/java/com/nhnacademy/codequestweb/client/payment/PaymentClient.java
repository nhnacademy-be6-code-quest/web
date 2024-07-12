package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * OrderPaymentRefund API 로 결제 정보를 저장하는 FeignClient 파일입니다. 주문 ID와 Toss Payments 결제 응답 정보를 전달받아
 * 서버에 저장합니다.
 *
 * @author 김채호
 * @version 1.0
 */
@FeignClient(name = "paymentClient", url = "http://localhost:8001")
public interface PaymentClient {

    /**
     * 주문 결제 정보를 서버에 저장하는 메서드입니다.
     *
     * @param orderId                 주문 ID (TossPayments 에서 주는 정보 아님)
     * @param tossPaymentsResponseDto Toss Payments 결제 응답 정보
     */
    @PostMapping("/api/order/{orderId}/payment")
    void savePayment(@RequestHeader HttpHeaders headers, @PathVariable long orderId,
                     @RequestBody TossPaymentsResponseDto tossPaymentsResponseDto);

    @GetMapping("/api/payment/grade/{clientId}")
    PaymentGradeResponseDto getPaymentRecordOfClient(@PathVariable Long clientId);
}