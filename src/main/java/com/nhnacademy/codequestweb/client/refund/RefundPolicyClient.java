package com.nhnacademy.codequestweb.client.refund;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "refundClient", url = "http://localhost:8001")
public interface RefundPolicyClient {

}
