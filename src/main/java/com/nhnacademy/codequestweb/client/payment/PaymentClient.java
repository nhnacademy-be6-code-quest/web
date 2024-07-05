package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * OrderPaymentRefund API 로 주문 결제 정보를 저장하는 FeignClient 파일입니다. 주문 ID와 Toss Payments 결제 응답 정보를 전달받아
 * 서버에 저장합니다.
 *
 * @author 김채호
 * @version 1.0
 */
@FeignClient(name = "paymentClient", url = "http://localhost:8001/api/client/order")
public interface PaymentClient {

    /**
     * 주문 결제 정보를 서버에 저장하는 메서드입니다.
     *
     * @param orderId                 주문 ID (TossPayments 에서 주는 정보 아님)
     * @param tossPaymentsResponseDto Toss Payments 결제 응답 정보
     */
    @PostMapping("{orderId}/payment")
    void savePayment(@PathVariable long orderId,
        @RequestBody TossPaymentsResponseDto tossPaymentsResponseDto);
}