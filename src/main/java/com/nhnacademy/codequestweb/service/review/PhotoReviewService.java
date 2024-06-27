package com.nhnacademy.codequestweb.service.review;


import com.nhnacademy.codequestweb.client.review.PhotoReviewClient;
import com.nhnacademy.codequestweb.request.review.PhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PhotoReviewService {

    private final PhotoReviewClient photoReviewClient;

    public ResponseEntity<PhotoReviewResponseDTO> createReview(HttpHeaders headers, PhotoReviewRequestDTO requestDTO) {
        return photoReviewClient.createReview(headers, requestDTO);
    }

    public ResponseEntity<PhotoReviewResponseDTO> getReviewById(HttpHeaders headers, Long id) {
        return photoReviewClient.getReviewById(headers, id);
    }

    public ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviews(HttpHeaders headers, Pageable pageable) {
        return photoReviewClient.getAllReviews(headers, pageable);
    }

    public ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByClientId(
        HttpHeaders headers, Pageable pageable) {
        return photoReviewClient.getAllReviewsByClientId(headers, pageable);
    }

    public ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByProductId(Long productId,
        Pageable pageable) {
        return photoReviewClient.getAllReviewsByProductId(productId, pageable);
    }

    public ResponseEntity<PhotoReviewResponseDTO> updateReview(HttpHeaders headers, Long id,
        PhotoReviewRequestDTO requestDTO) {
        return photoReviewClient.updateReview(headers, id, requestDTO);
    }

    public ResponseEntity<Void> deleteReview(HttpHeaders headers, Long id) {
        return photoReviewClient.deleteReview(headers, id);
    }

    public ResponseEntity<Boolean> hasWrittenReview(HttpHeaders headers, Long orderDetailId) {
        return photoReviewClient.hasWrittenReview(headers, orderDetailId);
    }

}
