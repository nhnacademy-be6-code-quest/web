package com.nhnacademy.codequestweb.client.payment;

import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "nhnKeyManagerClient", url = "https://api-keymanager.nhncloudservice.com/keymanager/v1.0/appkey")
public interface NhnKeyManagerClient {

    @GetMapping("/2SxwmBzUfnqJaA2A/secrets/2b7a4c66d9cd42079e99a88283416515")
    JSONObject getTossSecretKey();
}
