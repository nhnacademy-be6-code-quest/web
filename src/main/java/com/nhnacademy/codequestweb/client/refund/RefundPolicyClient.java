package com.nhnacademy.codequestweb.client.refund;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "refundClient", url = "http://localhost:8001")
public interface RefundPolicyClient {

}
