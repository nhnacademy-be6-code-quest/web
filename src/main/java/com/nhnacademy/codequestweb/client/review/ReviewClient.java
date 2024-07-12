package com.nhnacademy.codequestweb.client.review;

import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "review", url = "http://localhost:8001")
public interface ReviewClient {
    @PostMapping("/api/review")
    ResponseEntity<String> writeReview(
            @RequestBody WriteReviewRequestDto reviewWriteRequestDto,
            @RequestHeader("access") String access
    );
}
