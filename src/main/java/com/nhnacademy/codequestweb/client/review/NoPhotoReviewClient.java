package com.nhnacademy.codequestweb.client.review;


import com.nhnacademy.codequestweb.request.review.NoPhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.NoPhotoReviewResponseDTO;
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


@FeignClient(name = "noPhotoReviewClient", url = "http://localhost:8007/no-photo-reviews")
public interface NoPhotoReviewClient {

    @PostMapping
    ResponseEntity<NoPhotoReviewResponseDTO> createReview(
        @RequestBody NoPhotoReviewRequestDTO requestDTO);

    @GetMapping("/{id}")
    ResponseEntity<NoPhotoReviewResponseDTO> getReviewById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviews(Pageable pageable);

    @GetMapping("/client/{clientId}")
    ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviewsByClientId(
        @PathVariable Long clientId, Pageable pageable);

    @GetMapping("/product/{productId}")
    ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviewsByProductId(
        @PathVariable Long productId, Pageable pageable);

    @PutMapping("/{id}")
    ResponseEntity<NoPhotoReviewResponseDTO> updateReview(@PathVariable Long id,
        @RequestBody NoPhotoReviewRequestDTO requestDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReview(@PathVariable Long id);

    @GetMapping("/has-written/{orderDetailId}")
    ResponseEntity<Boolean> hasWrittenReview(@PathVariable Long orderDetailId);

}