package com.nhnacademy.codequestweb.client.review;

import com.nhnacademy.codequestweb.request.review.ReviewUpdateRequestDto;
import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "review", url = "http://localhost:8001")
public interface ReviewClient {
    @PostMapping("/api/review")
    ResponseEntity<String> writeReview(
            @RequestBody WriteReviewRequestDto reviewWriteRequestDto,
            @RequestHeader("access") String access
    );
    @GetMapping("/api/review/{orderDetailId}")
    ResponseEntity<Boolean> isWrited(
            @PathVariable Long orderDetailId,
            @RequestHeader("access") String access
    );
    @GetMapping("/api/reviews/my")
    ResponseEntity<Page<ReviewInfoResponseDto>> getMyReviews(
            @RequestParam int page,
            @RequestParam int size,
            @RequestHeader("access") String access
    );
    @GetMapping("/api/review")
    ResponseEntity<ReviewInfoResponseDto> getReviewInfo(
            @RequestParam Long reviewId,
            @RequestHeader("access") String access
    );
    @PutMapping("/api/review")
    ResponseEntity<String> updateReview(
            @RequestBody ReviewUpdateRequestDto reviewUpdateDto,
            @RequestHeader("access") String access
    );
    @GetMapping("/api/review/score")
    ResponseEntity<Double> getReviewScore(
            @RequestParam Long productId
    );
    @GetMapping("/api/reviews/product")
    ResponseEntity<Page<ReviewInfoResponseDto>> getReviewProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam Long productId
    );
}
