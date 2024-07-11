package com.nhnacademy.codequestweb.service.review;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductClient;
import com.nhnacademy.codequestweb.exception.product.ProductLoadFailException;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.response.order.common.OrderResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final OrderClient orderClient;
    private final BookProductClient bookProductClient;

    /**
     * 주문 상세를 받아서 상품 정보를 전달
     * 리뷰를 저장 할 때 필요한 별도의 상품아이디, 주문 상세 아이디를 제공
     *
     *
     * @return
     */
    public WriteReviewResponseDto writeReview(Long orderDetailId, String access) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", access);

        BookProductGetResponseDto bookProduct = getProductInfo(5L);
        return WriteReviewResponseDto.builder()
                .productOrderDetailId(null)
                .productId(bookProduct.productId())
                .title(bookProduct.title())
                .author(bookProduct.author())
                .cover(bookProduct.cover())
                .build();
    }

    private BookProductGetResponseDto getProductInfo(Long productId) {
        try {
            return bookProductClient.getSingleBookInfo(null, productId).getBody();
        } catch (Exception e) {
            throw new ProductLoadFailException("Could not get product info");
        }
    }
}
