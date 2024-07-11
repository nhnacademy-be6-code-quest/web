package com.nhnacademy.codequestweb.client.point;

import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationMyPageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="pointAccumulation", url="http://localhost:8001")

public interface PointAccumulationClient {
    @GetMapping("/api/point/myPage/reward")
    ResponseEntity<Page<PointAccumulationMyPageResponseDto>> findClientPoint(@RequestHeader
        HttpHeaders headers, @RequestParam int page, @RequestParam int size);

    @GetMapping("/api/point/adminPage/reward")
    ResponseEntity<Page<PointAccumulationAdminPageResponseDto>> findUserPoint(@RequestHeader
    HttpHeaders headers, @RequestParam int page, @RequestParam int size);

    @DeleteMapping("/api/point/adminPage/delete/{pointAccumulationHistoryId}")
    ResponseEntity<String> deleteUserPoint(@RequestHeader HttpHeaders headers, @PathVariable long pointAccumulationHistoryId);
}
