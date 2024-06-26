package com.nhnacademy.codequestweb.client.review;


import com.nhnacademy.codequestweb.request.review.PhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "photoReviewClient", url = "http://localhost:8001/photo-reviews")
public interface PhotoReviewClient {

    @PostMapping
    ResponseEntity<PhotoReviewResponseDTO> createReview(
        @RequestBody PhotoReviewRequestDTO requestDTO);

    @GetMapping("/{id}")
    ResponseEntity<PhotoReviewResponseDTO> getReviewById(@PathVariable("id") Long id);

    @GetMapping
    ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviews(Pageable pageable);

    @GetMapping("/client/{clientId}")
    ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByClientId(
        @PathVariable Long clientId, Pageable pageable);

    @GetMapping("/product/{productId}")
    ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByProductId(
        @PathVariable Long productId, Pageable pageable);

    @PutMapping("/{id}")
    ResponseEntity<PhotoReviewResponseDTO> updateReview(@PathVariable("id") Long id,
        @RequestBody PhotoReviewRequestDTO requestDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReview(@PathVariable("id") Long id);

}