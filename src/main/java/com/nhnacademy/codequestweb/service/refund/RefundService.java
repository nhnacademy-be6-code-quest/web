package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.RefundClient;
import com.nhnacademy.codequestweb.request.refund.PaymentCancelRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundAfterRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundRequestDto;
import com.nhnacademy.codequestweb.response.refund.RefundAdminResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundPolicyResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundSuccessResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundClient refundClient;
    public RefundAdminResponseDto findUserRefund(long orderId){
        return refundClient.refundAccessView(orderId);
    }


    public void cancelRequest(PaymentCancelRequestDto paymentCancelRequestDto){
        refundClient.paymentCancel(paymentCancelRequestDto);
    }
    public List<RefundPolicyResponseDto> findRefundPay(long orderId){
        return refundClient.refundInfo(orderId).getBody();
    }

    public RefundSuccessResponseDto requestRefund(RefundRequestDto requestDto){
        return refundClient.refundRequest(requestDto);
    }
    public RefundAdminResponseDto userRefund(long orderId){
        return refundClient.refundAccessView(orderId);
    }
    public void refundSure(RefundAfterRequestDto refundAfterRequestDto){
        refundClient.refundAccess(refundAfterRequestDto);
    }
}
