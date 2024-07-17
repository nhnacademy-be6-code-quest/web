package com.nhnacademy.codequestweb.service.shipping;

import com.nhnacademy.codequestweb.client.shipping.ShippingPolicyClient;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingPolicyService {

    private final ShippingPolicyClient shippingPolicyClient;

    public ShippingPolicyGetResponseDto getShippingPolicy(HttpHeaders headers, String type) {
        return shippingPolicyClient.getShippingPolicy(headers, type).getBody();
    }

}
