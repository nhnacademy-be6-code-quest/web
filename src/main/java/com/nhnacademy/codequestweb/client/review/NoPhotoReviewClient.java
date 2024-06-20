package com.nhnacademy.codequestweb.client.review;

import com.nhnacademy.codequestweb.request.review.NoPhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.NoPhotoReviewResponseDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
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
    ResponseEntity<NoPhotoReviewResponseDTO> getReviewById(@PathVariable("id") Long id);

    @GetMapping
    ResponseEntity<List<NoPhotoReviewResponseDTO>> getAllReviews();

    @PutMapping("/{id}")
    ResponseEntity<NoPhotoReviewResponseDTO> updateReview(@PathVariable("id") Long id,
        @RequestBody NoPhotoReviewRequestDTO requestDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReview(@PathVariable("id") Long id);

}