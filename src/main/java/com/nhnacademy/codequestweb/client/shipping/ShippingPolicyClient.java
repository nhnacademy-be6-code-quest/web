package com.nhnacademy.codequestweb.client.shipping;

import com.nhnacademy.codequestweb.response.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 배송정책 클라이언트
 * @author 박희원(bakhuiwon326)
 **/

@FeignClient(name = "shippingPolicy", url = "http://localhost:8001")
public interface ShippingPolicyClient {

    @PutMapping("/admin/shipping-policy")
    ResponseEntity<String> updateShippingPolicy(@RequestHeader HttpHeaders headers, @RequestBody AdminShippingPolicyPutRequestDto adminShippingPolicyPutRequestDto);

    @GetMapping("/api/shipping-policy")
    ResponseEntity<ShippingPolicyGetResponseDto> getShippingPolicy(@RequestHeader HttpHeaders headers, @RequestParam(name = "type", required = true) String type);

    @GetMapping("/api/shipping-policy/all")
    ResponseEntity<List<ShippingPolicyGetResponseDto>> getAllShippingPolicies(@RequestHeader HttpHeaders headers);

}
