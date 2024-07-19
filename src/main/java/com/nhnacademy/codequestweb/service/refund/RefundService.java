package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.RefundClient;
import com.nhnacademy.codequestweb.request.refund.PaymentCancelRequestDto;
import com.nhnacademy.codequestweb.response.refund.PaymentCancelResponseDto;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundClient refundClient;

    public PaymentCancelResponseDto findTossKey(long orderId) throws ParseException {
        PaymentCancelResponseDto dto = refundClient.findPaymentKey(orderId).getBody();
        return dto;
    }


    public void paymentCancelSave(long orderId,PaymentCancelResponseDto paymentCancelResponseDto, String access){
        PaymentCancelRequestDto dto = PaymentCancelRequestDto.builder()
            .orderStatus(paymentCancelResponseDto.getOrderStatus())
            .orderId(orderId).build();
        refundClient.paymentCancel(access, dto);

    }
    public void requestRefund(long orderId){

    }

}
