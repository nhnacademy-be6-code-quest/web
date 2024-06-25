package com.nhnacademy.codequestweb.service.review;


import com.nhnacademy.codequestweb.client.review.PhotoReviewClient;
import com.nhnacademy.codequestweb.request.review.PhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PhotoReviewService {

    private final PhotoReviewClient photoReviewClient;

    public ResponseEntity<PhotoReviewResponseDTO> createReview(PhotoReviewRequestDTO requestDTO) {
        return photoReviewClient.createReview(requestDTO);
    }

    public ResponseEntity<PhotoReviewResponseDTO> getReviewById(Long id) {
        return photoReviewClient.getReviewById(id);
    }

    public ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviews(Pageable pageable) {
        return photoReviewClient.getAllReviews(pageable);
    }

    public ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByClientId(Long clientId,
        Pageable pageable) {
        return photoReviewClient.getAllReviewsByClientId(clientId, pageable);
    }

    public ResponseEntity<Page<PhotoReviewResponseDTO>> getAllReviewsByProductId(Long productId,
        Pageable pageable) {
        return photoReviewClient.getAllReviewsByProductId(productId, pageable);
    }

    public ResponseEntity<PhotoReviewResponseDTO> updateReview(Long id,
        PhotoReviewRequestDTO requestDTO) {
        return photoReviewClient.updateReview(id, requestDTO);
    }

    public ResponseEntity<Void> deleteReview(Long id) {
        return photoReviewClient.deleteReview(id);
    }

    public ResponseEntity<Boolean> hasWrittenReview(Long orderDetailId) {
        return photoReviewClient.hasWrittenReview(orderDetailId);
    }

}
