package com.nhnacademy.codequestweb.service.shippingpolicy;

import com.nhnacademy.codequestweb.client.shipping.ShippingPolicyClient;
import com.nhnacademy.codequestweb.response.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingPolicyServiceImpl implements ShippingPolicyService {

    private final ShippingPolicyClient shippingPolicyClient;

    @Override
    public void updateShippingPolicy(AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto) {
        shippingPolicyClient.updateShippingPolicy(adminShippingPolicyPutRequestDto);
    }

    @Override
    public ShippingPolicyGetResponseDto getShippingPolicy(String type) {
        return shippingPolicyClient.getShippingPolicy(type).getBody();
    }

    @Override
    public List<ShippingPolicyGetResponseDto> getAllShippingPolicies() {
        return shippingPolicyClient.getAllShippingPolicies().getBody();
    }
}
