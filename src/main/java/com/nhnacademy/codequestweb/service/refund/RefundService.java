package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.RefundClient;
import com.nhnacademy.codequestweb.request.refund.RefundRegisterRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundTossRequestDto;
import com.nhnacademy.codequestweb.response.refund.PaymentRefundResponseDto;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundClient refundClient;
    private final TossRefundService tossRefundService;

    public PaymentRefundResponseDto findTossKey(long orderId) throws ParseException {
        PaymentRefundResponseDto dto = refundClient.findPaymentKey(orderId).getBody();
        return dto;
    }


    public void saveRefund(long orderId, RefundTossRequestDto request){
        RefundRegisterRequestDto dto = RefundRegisterRequestDto.builder()
            .cancelReason(request.getCancelReason())
            .paymentId(request.getPaymentId())
            .orderStatus(request.getOrderStatus())
            .orderId(orderId).build();

    }
    public void requestRefund(long orderId){

    }

}
