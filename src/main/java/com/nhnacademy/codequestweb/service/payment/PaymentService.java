package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.payment.PaymentClient;
import com.nhnacademy.codequestweb.request.payment.PaymentRequestDto;
import com.nhnacademy.codequestweb.response.payment.OrderPaymentResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;

    // Post
    public void savePayment(PaymentRequestDto paymentRequestDto) {
        paymentClient.savePayment(paymentRequestDto); // 여기까지 들어가는 것 확인했음.
    }

    // PaymentId 로 Payment 관련 정보 얻기
    public ResponseEntity<PaymentResponseDto> findByPaymentId(Long paymentId) {
        return paymentClient.findByPaymentId(paymentId);
    }

    public OrderPaymentResponseDto findOrderPaymentResponseDtoByOrderId(long orderId) {
        return paymentClient.findOrderPaymentResponseDtoByOrderId(orderId);
    }
}