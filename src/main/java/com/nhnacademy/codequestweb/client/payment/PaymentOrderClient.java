package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Order API 로 주문 관련 Request 를 보내는 FeignClient 파일입니다. 결제와 같은 서비스이기는 하나, 가독성 및 충돌 우려를 위해 분리했습니다.
 * Payment -> Order API 에 요청을 보내기 때문에 PaymentOrderClient 라는 네이밍을 사용하게 되었습니다.
 *
 * @author 김채호
 * @version 0.0
 */
@FeignClient(name = "paymentOrder", url = "http://localhost:8001")
public interface PaymentOrderClient {

    @GetMapping("/api/client/views/order")
    PaymentOrderShowRequestDto findPaymentOrderShowRequestDtoByOrderId(@RequestHeader HttpHeaders headers, long orderId);

    @GetMapping("/api/client/views/order")
    PaymentOrderApproveRequestDto findPaymentOrderApproveRequestDtoByOrderId(@RequestHeader HttpHeaders headers,long orderId);

    @GetMapping("/api/client/views/order")
    ResponseEntity<String> changeOrderStatusCompletePayment(@RequestHeader HttpHeaders headers, Long orderId);
}
