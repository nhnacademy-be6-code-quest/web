package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.payment.*;
import com.nhnacademy.codequestweb.request.payment.ApprovePaymentRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.viewRequest.PaymentViewRequestDto;
import com.nhnacademy.codequestweb.request.point.PointRewardOrderRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentMethodResponseDto;
import com.nhnacademy.codequestweb.response.payment.approve.PaymentApproveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentOrderClient paymentOrderClient;
    private final PaymentPointClient paymentPointClient;
    private final OrderClient orderClient;

    public PaymentApproveResponseDto approvePayment(HttpHeaders headers, String orderCode, String pgName, Map<String, String[]> reqParamMap) {
        ApprovePaymentRequestDto approvePaymentRequestDto = ApprovePaymentRequestDto.builder()
                .orderCode(orderCode)
                .pgName(pgName)
                .reqParamMap(reqParamMap)
                .build();
        return paymentClient.approvePayment(headers, approvePaymentRequestDto).getBody();
    }

    public PaymentViewRequestDto getPaymentViewRequest(HttpHeaders headers, String orderCode, String pgName) {
        return orderClient.getPaymentViewRequestDto(headers, orderCode, pgName).getBody();
    }

    public ResponseEntity<String> usePaymentPoint(
        PaymentUsePointRequestDto paymentUsePointRequestDto, HttpHeaders httpHeaders) {
        return paymentPointClient.usePaymentPoint(paymentUsePointRequestDto, httpHeaders);
    }

    public ResponseEntity<String> accumulatePoint(HttpHeaders httpHeaders, Long amount) {
        PointRewardOrderRequestDto pointRewardOrderRequestDto = new PointRewardOrderRequestDto();
        pointRewardOrderRequestDto.setAccumulatedPoint(amount);
        return paymentPointClient.rewardOrderPoint(httpHeaders, pointRewardOrderRequestDto);
    }

    public ResponseEntity<String> changeOrderStatusCompletePayment(Long orderId, String status) {
        return paymentOrderClient.updateOrderStatus(orderId, status);
    }

    public PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId) {
        return paymentClient.getPaymentRecordOfClient(clientId).getBody();
    }

    public List<PaymentMethodResponseDto> getPaymentMethodList(HttpHeaders headers) {
        return paymentClient.getAllPaymentMethod(headers).getBody();
    }

}