package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * OrderPaymentRefund API 로 인도하는 FeignClient 파일입니다.
 *
 * @author 김채호
 * @version 1.0
 */
@FeignClient(name = "paymentClient", url = "http://localhost:8001/api/client/order")
public interface PaymentClient {

    @PostMapping("{orderId}/payment")
    void savePayment(@PathVariable long orderId,
        @RequestBody TossPaymentsResponseDto tossPaymentsResponseDto);
}