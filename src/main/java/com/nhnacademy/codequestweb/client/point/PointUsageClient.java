package com.nhnacademy.codequestweb.client.point;

import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import com.nhnacademy.codequestweb.response.point.PointUsageMyPageResponseDto;

@FeignClient(name = "clientCoupon", url = "http://localhost:8001")

public interface PointUsageClient {

    @GetMapping("/api/point/myPage/use")
    ResponseEntity<Page<PointUsageMyPageResponseDto>> usedClientPoint(
        @RequestHeader HttpHeaders headers,
        @RequestParam int page,
        @RequestParam int size);
    @GetMapping("/api/point/adminPage/use")
    ResponseEntity<Page<PointUsageAdminPageResponseDto>> usedUserPoint(
        @RequestParam int page,

        @RequestParam int size);

}
