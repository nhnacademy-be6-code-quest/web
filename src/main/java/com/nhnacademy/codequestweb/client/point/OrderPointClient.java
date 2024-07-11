package com.nhnacademy.codequestweb.client.point;

import com.nhnacademy.codequestweb.request.point.PointRewardOrderRequestDto;
import com.nhnacademy.codequestweb.request.point.PointUsagePaymentRequestDto;
import com.nhnacademy.codequestweb.response.point.TotalPointAmountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="pointOrder", url="http://localhost:8001")
public interface OrderPointClient {
    @GetMapping("/api/point")
    TotalPointAmountResponseDto findPoint(@RequestHeader HttpHeaders headers);

    @PostMapping("/api/point/order")
    ResponseEntity<String> rewardOrderPoint(@RequestHeader HttpHeaders headers,
        @RequestBody PointRewardOrderRequestDto pointRewardOrderRequestDto);

    @PostMapping("/api/point/use/payment")
    ResponseEntity<String> usePaymentPoint(
        @RequestBody PointUsagePaymentRequestDto pointUsagePaymentRequestDto, @RequestHeader
    HttpHeaders headers);
}
