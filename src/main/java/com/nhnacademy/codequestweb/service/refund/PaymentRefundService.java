package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.RefundOrderClient;
import com.nhnacademy.codequestweb.request.refund.RefundTossRequestDto;
import com.nhnacademy.codequestweb.response.refund.PaymentRefundResponseDto;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentRefundService {

    private final RefundOrderClient refundOrderClient;
    private final TossRefundService tossRefundService;

    public PaymentRefundResponseDto findTossKey(long orderId) throws ParseException {
        PaymentRefundResponseDto dto = refundOrderClient.findPaymentKey(orderId).getBody();
        return dto;
    }


}
