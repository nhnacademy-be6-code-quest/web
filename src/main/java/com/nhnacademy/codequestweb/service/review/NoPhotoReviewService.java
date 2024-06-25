package com.nhnacademy.codequestweb.service.review;


import com.nhnacademy.codequestweb.client.review.NoPhotoReviewClient;
import com.nhnacademy.codequestweb.request.review.NoPhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.NoPhotoReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class NoPhotoReviewService {

    private final NoPhotoReviewClient noPhotoReviewClient;


    public ResponseEntity<NoPhotoReviewResponseDTO> createReview(
        NoPhotoReviewRequestDTO requestDTO) {
        return noPhotoReviewClient.createReview(requestDTO);
    }

    public ResponseEntity<NoPhotoReviewResponseDTO> getReviewById(Long id) {
        return noPhotoReviewClient.getReviewById(id);
    }

    public ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviews(Pageable pageable) {
        return noPhotoReviewClient.getAllReviews(pageable);
    }

    public ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviewsByClientId(Long clientId,
        Pageable pageable) {
        return noPhotoReviewClient.getAllReviewsByClientId(clientId, pageable);
    }

    public ResponseEntity<Page<NoPhotoReviewResponseDTO>> getAllReviewsByProductId(Long productId,
        Pageable pageable) {
        return noPhotoReviewClient.getAllReviewsByProductId(productId, pageable);
    }

    public ResponseEntity<NoPhotoReviewResponseDTO> updateReview(Long id,
        NoPhotoReviewRequestDTO requestDTO) {
        return noPhotoReviewClient.updateReview(id, requestDTO);
    }

    public ResponseEntity<Void> deleteReview(Long id) {
        return noPhotoReviewClient.deleteReview(id);
    }

    public ResponseEntity<Boolean> hasWrittenReview(Long orderDetailId) {
        return noPhotoReviewClient.hasWrittenReview(orderDetailId);
    }

}
