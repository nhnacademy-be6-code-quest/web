package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.RefundClient;
import com.nhnacademy.codequestweb.request.refund.RefundOrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundClient refundClient;

    public RefundOrderRequestDto findRefundOrderRequestDtoByOrderId(long orderId) {
        return refundClient.findRefundOrderRequestDtoByOrderId(orderId);
    }
}
