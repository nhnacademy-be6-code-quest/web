package com.nhnacademy.codequestweb.client.refund;

import com.nhnacademy.codequestweb.request.refund.PaymentCancelRequestDto;
import com.nhnacademy.codequestweb.response.refund.PaymentCancelResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundPolicyResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundAdminResponseDto;
import com.nhnacademy.codequestweb.request.refund.RefundRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundAfterRequestDto;
import com.nhnacademy.codequestweb.response.refund.RefundSuccessResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "refundOrderClient", url = "http://localhost:8001")
public interface RefundClient {

    @GetMapping("/api/refund")
    ResponseEntity<PaymentCancelResponseDto> findPaymentKey(@RequestParam long orderId);


    @PostMapping("/api/refund/cancel")
    void paymentCancel(@RequestBody PaymentCancelRequestDto paymentCancelRequestDto);

    @GetMapping("/api/refund/request")
    ResponseEntity<List<RefundPolicyResponseDto>> refundInfo(@RequestParam long orderId);


    @PostMapping("/api/refund/request")
    RefundSuccessResponseDto refundRequest(@RequestBody RefundRequestDto requestDto);

    @GetMapping("/api/refund/admin/refund")
    RefundAdminResponseDto refundAccessView(@RequestParam long orderId);

    @PostMapping("/api/refund/admin/refund")
    void refundAccess(@RequestBody RefundAfterRequestDto refundAfterRequestDto);

}
