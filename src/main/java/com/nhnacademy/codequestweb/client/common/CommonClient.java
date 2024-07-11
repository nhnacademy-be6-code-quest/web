package com.nhnacademy.codequestweb.client.common;

import com.nhnacademy.codequestweb.response.common.ClientRoleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "common", url = "http://localhost:8001")
public interface CommonClient {
    @GetMapping("/api/client/role")
    ResponseEntity<ClientRoleResponseDto> getClientRole(@RequestHeader HttpHeaders headers);
}
