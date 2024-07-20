package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.codequestweb.request.payment.TossApprovePaymentRequest;
import com.nhnacademy.codequestweb.request.payment.UserUpdateGradeRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import org.json.simple.parser.ParseException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param tossPaymentsResponseDto Toss Payments 결제 응답 정보
     */
    @PostMapping("/api/order/payment/save")
    void savePayment(@RequestHeader HttpHeaders headers, @RequestBody TossPaymentsResponseDto tossPaymentsResponseDto);

    @GetMapping("/api/payment/grade/{clientId}")
    ResponseEntity<PaymentGradeResponseDto> getPaymentRecordOfClient(@PathVariable Long clientId);

    @PostMapping("/api/order/payment/approve")
    ResponseEntity<TossPaymentsResponseDto> approvePayment(@RequestHeader HttpHeaders headers, @RequestBody TossApprovePaymentRequest tossApprovePaymentRequest);

    @GetMapping("/api/order/payment/post-process")
    ResponseEntity<PostProcessRequiredPaymentResponseDto> getPostProcessRequiredPaymentResponseDto(@RequestParam("tossOrderId") String tossOrderId);

    @PostMapping("/api/order/update/user")
    void updateUser(@RequestBody UserUpdateGradeRequestDto userUpdateGradeRequestDto);
}