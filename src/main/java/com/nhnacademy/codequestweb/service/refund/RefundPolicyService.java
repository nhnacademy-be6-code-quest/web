package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.RefundPolicyClient;
import com.nhnacademy.codequestweb.request.refund.RefundPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.refund.RefundPolicyListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundPolicyService {
    private final RefundPolicyClient refundPolicyClient;

    public void savePolicy (RefundPolicyRegisterRequestDto requestDto){
        refundPolicyClient.saveRefundPolicy(requestDto);
    }

    public Page<RefundPolicyListResponseDto> findPolices(int page, int size){
        return refundPolicyClient.findAllRefundPolicy(page, size).getBody();
    }
}
