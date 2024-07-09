package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.RefundClient;
import com.nhnacademy.codequestweb.client.refund.RefundOrderClient;
import com.nhnacademy.codequestweb.controller.refund.RefundPolicyRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

//    private final RefundClient refundClient;
    private final RefundOrderClient refundOrderClient;
    private final RefundClient refundClient;

//    public RefundOrderRequestDto findRefundOrderRequestDtoByOrderId(long orderId) {
//        return refundClient.findRefundOrderRequestDtoByOrderId(orderId);
//    }
//
//    public String findOrderStatusByOrderId(Long orderId) {
//        return refundClient.findOrderStatusByOrderId(orderId);
//    }

    public String getOrderStatus(long orderId) {
        return refundOrderClient.findOrderStatusByOrderId(orderId);
    }

    public boolean isRefund(String orderStatus) {
        return orderStatus.equals("배송중") || orderStatus.equals("배송완료");
    }

    public boolean isCancel(String orderStatus) {
        return orderStatus.equals("결제대기") || orderStatus.equals("결제완료");
    }

    public List<RefundPolicyRequestDto> findAllRefundPolicyRequestDtoList() {
        return refundClient.findAllRefundPolicyRequestDtoList();
    }
}
