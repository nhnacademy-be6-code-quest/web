package com.nhnacademy.codequestweb.client.admin;

import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "admin", url = "http://localhost:8001")
public interface AdminClient {
    @GetMapping("/api/client/privacy-page")
    ResponseEntity<Page<ClientPrivacyResponseDto>> privacyPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam boolean desc,
            @RequestHeader(name = "access") String access
    );
}
