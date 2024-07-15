package com.nhnacademy.codequestweb.client.point;

import com.nhnacademy.codequestweb.request.point.PointPolicyActiveRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyModifyRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyDetailResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pointPolicy", url = "http://localhost:8001")
public interface PointPolicyClient {

    @PostMapping("/api/point/policy/register")
    ResponseEntity<PointPolicyRegisterRequestDto> savePointPolicy(
        @RequestHeader HttpHeaders headers,
        @RequestBody PointPolicyRegisterRequestDto pointPolicyRegisterRequestDto);

    @GetMapping("/api/point/policy")
    ResponseEntity<Page<PointPolicyAdminListResponseDto>> findAllPointPolicy(@RequestHeader
    HttpHeaders headers, @RequestParam int page, @RequestParam int size);

    @GetMapping("/api/point/policy/{pointPolicyId}")
    ResponseEntity<PointPolicyDetailResponseDto> findPointPolicy(@PathVariable long pointPolicyId);

    @PutMapping("/api/point/policy/modify")
    ResponseEntity<String> modifyPointPolicy(
        @RequestBody PointPolicyModifyRequestDto pointPolicyModifyRequestDto);

    @PutMapping("/api/point/policy/active")
    ResponseEntity<String> pointPolicyActive(
        @RequestBody PointPolicyActiveRequestDto pointPolicyActiveRequestDto);
}
