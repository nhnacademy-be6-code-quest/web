package com.nhnacademy.codequestweb.client.review;


import com.nhnacademy.codequestweb.request.review.PhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "photoReviewClient", url = "http://localhost:8001/no-photo-reviews")
//@FeignClient(name = "photoReviewClient", url = "http://10.220.222.13:8001/photo-reviews")
public interface PhotoReviewClient {

    @PostMapping
    ResponseEntity<PhotoReviewResponseDTO> createReview(
        @RequestHeader HttpHeaders headers, @RequestBody PhotoReviewRequestDTO requestDTO);

    @GetMapping("/{id}")
    ResponseEntity<PhotoReviewResponseDTO> getReviewById(@RequestHeader HttpHeaders headers, @PathVariable("id") Long id);

    @GetMapping
    ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviews(@RequestHeader HttpHeaders headers, Pageable pageable);

    @GetMapping("/client")
    ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByClientId(
        @RequestHeader HttpHeaders headers, Pageable pageable);

    @GetMapping("/product/{productId}")
    ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByProductId(
        @PathVariable Long productId, Pageable pageable);

    @PutMapping("/{id}")
    ResponseEntity<PhotoReviewResponseDTO> updateReview(@RequestHeader HttpHeaders headers, @PathVariable("id") Long id,
        @RequestBody PhotoReviewRequestDTO requestDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReview(@RequestHeader HttpHeaders headers, @PathVariable("id") Long id);

    @GetMapping("/has-written/{orderDetailId}")
    ResponseEntity<Boolean> hasWrittenReview(@RequestHeader HttpHeaders headers, @PathVariable Long orderDetailId);

}