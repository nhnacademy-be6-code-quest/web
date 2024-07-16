package com.nhnacademy.codequestweb.client.refund;

import com.nhnacademy.codequestweb.request.refund.RefundPolicyRequestDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "refundClient", url = "http://localhost:8001")
public interface RefundPolicyClient {

    @PostMapping("/api/order/{orderId}/payment/{paymentId}/refund")
    void saveRefund(@PathVariable long orderId, @PathVariable long paymentId);

    @GetMapping("/api/refund/refund-policy")
    List<RefundPolicyRequestDto> findAllRefundPolicyRequestDtoList();
}
