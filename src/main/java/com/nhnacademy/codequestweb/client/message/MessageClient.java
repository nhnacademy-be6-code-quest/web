package com.nhnacademy.codequestweb.client.message;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "message", url = "http://localhost:8001")
public interface MessageClient {
    @GetMapping("/send/change-password")
    ResponseEntity<String> sendChangePassword(@RequestParam("email") String email);
    @GetMapping("/send/recover-account")
    ResponseEntity<String> sendRecoverAccount(@RequestParam("email") String email);
}
