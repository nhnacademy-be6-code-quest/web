package com.nhnacademy.codequestweb.service.shippingpolicy;

import com.nhnacademy.codequestweb.client.shipping.ShippingPolicyClient;
import com.nhnacademy.codequestweb.response.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingPolicyServiceImpl implements ShippingPolicyService {

    private final ShippingPolicyClient shippingPolicyClient;

    @Override
    public void updateShippingPolicy(HttpHeaders headers, AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto) {
        shippingPolicyClient.updateShippingPolicy(headers, adminShippingPolicyPutRequestDto);
    }

    @Override
    public ShippingPolicyGetResponseDto getShippingPolicy(HttpHeaders headers, String type) {
        return shippingPolicyClient.getShippingPolicy(headers, type).getBody();
    }

    @Override
    public List<ShippingPolicyGetResponseDto> getAllShippingPolicies(HttpHeaders headers) {
        return shippingPolicyClient.getAllShippingPolicies(headers).getBody();
    }

}
