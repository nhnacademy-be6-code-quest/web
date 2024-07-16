package com.nhnacademy.codequestweb.client.refund;

import com.nhnacademy.codequestweb.request.refund.RefundTossRequestDto;
import com.nhnacademy.codequestweb.request.refund.TossRefundRequestDto;
import com.nhnacademy.codequestweb.response.refund.TossPaymentRefundResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "tossRefundClient", url = "https://api.tosspayments.com/v1/payments")
public interface TossPayRefundClient {

    @PostMapping("/{paymentKey}/cancel")
    TossPaymentRefundResponseDto cancelPayment(@PathVariable String paymentKey, @RequestBody TossRefundRequestDto tossRefundRequestDto, @RequestHeader String authorization,
        @RequestHeader String contentType);
}
