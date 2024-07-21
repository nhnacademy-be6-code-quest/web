package com.nhnacademy.codequestweb.service.review;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.product.book.BookProductClient;
import com.nhnacademy.codequestweb.client.review.ReviewClient;
import com.nhnacademy.codequestweb.exception.order.ProductOrderDetailLoadFailException;
import com.nhnacademy.codequestweb.exception.product.ProductLoadFailException;
import com.nhnacademy.codequestweb.request.review.ReviewUpdateRequestDto;
import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.order.common.ProductOrderDetailResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final OrderClient orderClient;
    private final ReviewClient reviewClient;
    private final BookProductClient bookProductClient;

    public WriteReviewResponseDto writeReview(Long orderId, Long orderDetailId, String access) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", access);

        ProductOrderDetailResponseDto orderDetail = getOrderDetail(orderId, orderDetailId, access);
        BookProductGetResponseDto bookProduct = getProductInfo(orderDetail.getProductId());
        return WriteReviewResponseDto.builder()
                .productOrderDetailId(orderDetailId)
                .productId(bookProduct.productId())
                .title(bookProduct.title())
                .author(bookProduct.author())
                .cover(bookProduct.cover())
                .build();
    }

    private ProductOrderDetailResponseDto getOrderDetail(Long orderId, Long orderDetailId, String access) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", access);
        try {
            return orderClient.getClientProductOrderDetail(headers, orderId, orderDetailId).getBody();
        } catch (Exception e) {
            throw new ProductOrderDetailLoadFailException(e.getMessage());
        }
    }

    public BookProductGetResponseDto getProductInfo(Long productId) {
        try {
            return bookProductClient.getSingleBookInfo(null, productId).getBody();
        } catch (Exception e) {
            throw new ProductLoadFailException("Could not get product info");
        }
    }

    public String posttingReview(WriteReviewRequestDto writeReviewRequestDto, String access) {
        try {
            return reviewClient.writeReview(writeReviewRequestDto, access).getBody();
        } catch (Exception e) {
            log.info("Could not post review : {}", e.getMessage());
            return "이미 작성된 리뷰입니다.";
        }
    }

    public boolean isWrited(String access, Long orderDetailId) {
        return reviewClient.isWrited(orderDetailId, access).getBody();
    }

    public ReviewInfoResponseDto getReviewInfo(String access, Long orderDetailId) {
        return reviewClient.getReviewInfo(orderDetailId, access).getBody();
    }

    public String updateReview(ReviewUpdateRequestDto reviewUpdateRequestDto, String access) {
        return reviewClient.updateReview(reviewUpdateRequestDto, access).getBody();
    }

    public Double getReviewScore(Long productId) {
        return reviewClient.getReviewScore(productId).getBody();
    }

    public Page<ReviewInfoResponseDto> getProductReviewPage(int page, int size, Long productId) {
        return reviewClient.getReviewProducts(page, size, productId).getBody();
    }
}
