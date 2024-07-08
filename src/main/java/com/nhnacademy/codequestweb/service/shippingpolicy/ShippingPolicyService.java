package com.nhnacademy.codequestweb.service.shippingpolicy;

import com.nhnacademy.codequestweb.response.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;

import java.util.List;

public interface ShippingPolicyService {
    void updateShippingPolicy(AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto);
    ShippingPolicyGetResponseDto getShippingPolicy(String type);
    List<ShippingPolicyGetResponseDto> getAllShippingPolicies();
}
