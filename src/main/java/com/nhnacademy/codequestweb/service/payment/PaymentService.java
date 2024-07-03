package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.payment.PaymentClient;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderValidationRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;

    public void savePayment(long orderId, PaymentResponseDto paymentResponseDto) {
        paymentClient.savePayment(orderId, paymentResponseDto);
    }

    public PaymentOrderRequestDto findOrderPaymentResponseDtoByOrderId(long orderId) {
        return paymentClient.findOrderPaymentResponseDtoByOrderId(orderId);
    }

    public PaymentOrderValidationRequestDto findOrderTossPaymentResponseDtoByOrderId(long orderId) {
        return paymentClient.findOrderTossPaymentResponseDtoByOrderId(orderId);
    }

    // PaymentId 로 Payment 관련 정보 얻기
//    public ResponseEntity<PaymentResponseDto> findByPaymentId(Long paymentId) {
//        return paymentClient.findByPaymentId(paymentId);
//    }

//    public OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(long orderId) {
//        return paymentClient.findOrderPaymentResponseDtoByOrderId(orderId);
//    }
}