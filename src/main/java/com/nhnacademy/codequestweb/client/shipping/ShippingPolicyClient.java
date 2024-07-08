package com.nhnacademy.codequestweb.client.shipping;

import com.nhnacademy.codequestweb.response.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 배송정책 클라이언트
 * @author 박희원(bakhuiwon326)
 **/

@FeignClient(name = "shippingPolicy", url = "http://localhost:8001")
public interface ShippingPolicyClient {

    @PutMapping("/admin/shipping-policy")
    ResponseEntity<String> updateShippingPolicy(@RequestBody AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto);

    @GetMapping("/shipping-policy")
    ResponseEntity<ShippingPolicyGetResponseDto> getShippingPolicy(@RequestParam(name = "type", required = true) String type);

    @GetMapping("/shipping-policy/all")
    ResponseEntity<List<ShippingPolicyGetResponseDto>> getAllShippingPolicies();

}
