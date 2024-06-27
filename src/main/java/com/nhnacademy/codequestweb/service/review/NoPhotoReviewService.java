package com.nhnacademy.codequestweb.service.review;


import com.nhnacademy.codequestweb.client.review.NoPhotoReviewClient;
import com.nhnacademy.codequestweb.request.review.NoPhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.NoPhotoReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;


@RequiredArgsConstructor
@Service
public class NoPhotoReviewService {

    private final NoPhotoReviewClient noPhotoReviewClient;


    public ResponseEntity<NoPhotoReviewResponseDTO> createReview(
        HttpHeaders headers, NoPhotoReviewRequestDTO requestDTO) {
        return noPhotoReviewClient.createReview(headers, requestDTO);
    }

    public ResponseEntity<NoPhotoReviewResponseDTO> getReviewById(HttpHeaders headers, Long id) {
        return noPhotoReviewClient.getReviewById(headers, id);
    }

    public ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviews(HttpHeaders headers, Pageable pageable) {
        return noPhotoReviewClient.getAllReviews(headers, pageable);
    }

    public ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviewsByClientId(
        HttpHeaders headers, Pageable pageable) {
        return noPhotoReviewClient.getAllReviewsByClientId(headers, pageable);
    }

    public ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviewsByProductId(Long productId,
        Pageable pageable) {
        return noPhotoReviewClient.getAllReviewsByProductId(productId, pageable);
    }

    public ResponseEntity<NoPhotoReviewResponseDTO> updateReview(HttpHeaders headers, Long id,
        NoPhotoReviewRequestDTO requestDTO) {
        return noPhotoReviewClient.updateReview(headers, id, requestDTO);
    }

    public ResponseEntity<Void> deleteReview(HttpHeaders headers, Long id) {
        return noPhotoReviewClient.deleteReview(headers, id);
    }

    public ResponseEntity<Boolean> hasWrittenReview(HttpHeaders headers, Long orderDetailId) {
        return noPhotoReviewClient.hasWrittenReview(headers, orderDetailId);
    }

}
