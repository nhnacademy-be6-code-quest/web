package com.nhnacademy.codequestweb.client.refund;

import com.nhnacademy.codequestweb.request.refund.RefundPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.refund.RefundPolicyListResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "refundClient", url = "http://localhost:8001")
public interface RefundPolicyClient {
    void saveRefundPolicy (@RequestBody RefundPolicyRegisterRequestDto requestDto);
    ResponseEntity<Page<RefundPolicyListResponseDto>> findAllRefundPolicy (@RequestParam int page, @RequestParam int size);
}
