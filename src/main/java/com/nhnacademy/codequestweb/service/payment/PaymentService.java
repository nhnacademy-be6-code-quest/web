package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.payment.PaymentClient;
import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;

    public void savePayment(long orderId, PaymentRequestDto paymentRequestDto) {
        paymentClient.savePayment(orderId, paymentRequestDto);
    }

    // PaymentId 로 Payment 관련 정보 얻기
//    public ResponseEntity<PaymentResponseDto> findByPaymentId(Long paymentId) {
//        return paymentClient.findByPaymentId(paymentId);
//    }

//    public OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(long orderId) {
//        return paymentClient.findOrderPaymentResponseDtoByOrderId(orderId);
//    }
}