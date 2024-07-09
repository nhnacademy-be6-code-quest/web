package com.nhnacademy.codequestweb.service.shippingpolicy;

import com.nhnacademy.codequestweb.response.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface ShippingPolicyService {
    void updateShippingPolicy(HttpHeaders headers, AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto);
    ShippingPolicyGetResponseDto getShippingPolicy(HttpHeaders headers, String type);
    List<ShippingPolicyGetResponseDto> getAllShippingPolicies(HttpHeaders headers);
}
