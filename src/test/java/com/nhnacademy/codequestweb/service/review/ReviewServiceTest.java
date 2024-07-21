package com.nhnacademy.codequestweb.service.review;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.product.book.BookProductClient;
import com.nhnacademy.codequestweb.client.review.ReviewClient;
import com.nhnacademy.codequestweb.exception.product.ProductLoadFailException;
import com.nhnacademy.codequestweb.request.review.ReviewUpdateRequestDto;
import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.order.common.ProductOrderDetailResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    @Mock
    private OrderClient orderClient;

    @Mock
    private ReviewClient reviewClient;

    @Mock
    private BookProductClient bookProductClient;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWriteReview() throws NoSuchFieldException, IllegalAccessException {
        Long orderId = 1L;
        Long orderDetailId = 2L;
        String access = "token";
        ProductOrderDetailResponseDto orderDetail = new ProductOrderDetailResponseDto();
        Class<?> clazz = ProductOrderDetailResponseDto.class;
        Field productIdField = clazz.getDeclaredField("productId");
        productIdField.setAccessible(true);
        productIdField.set(orderDetail, 3L);
        BookProductGetResponseDto bookProduct = BookProductGetResponseDto.builder()
                .productId(3L)
                .title("Title")
                .author("Author")
                .cover("Cover")
                .build();

        when(orderClient.getClientProductOrderDetail(any(), eq(orderId), eq(orderDetailId))).thenReturn(ResponseEntity.ok(orderDetail));
        when(bookProductClient.getSingleBookInfo(any(), eq(3L))).thenReturn(ResponseEntity.ok(bookProduct));

        WriteReviewResponseDto result = reviewService.writeReview(orderId, orderDetailId, access);

        assertNotNull(result);
        assertEquals(orderDetailId, result.getProductOrderDetailId());
        assertEquals(3L, result.getProductId());
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals("Cover", result.getCover());
    }

    @Test
    void testGetProductInfoFailure() {
        when(bookProductClient.getSingleBookInfo(null, 1L)).thenThrow(new RuntimeException("API Error"));

        assertThrows(ProductLoadFailException.class, () -> reviewService.getProductInfo(1L));
    }

    @Test
    void testPosttingReview() {
        WriteReviewRequestDto requestDto = new WriteReviewRequestDto();
        String access = "token";
        when(reviewClient.writeReview(requestDto, access)).thenReturn(ResponseEntity.ok("Success"));

        String result = reviewService.posttingReview(requestDto, access);

        assertEquals("Success", result);
    }

    @Test
    void testPosttingReviewFailure() {
        WriteReviewRequestDto requestDto = new WriteReviewRequestDto();
        String access = "token";
        when(reviewClient.writeReview(requestDto, access)).thenThrow(new RuntimeException("API Error"));

        String result = reviewService.posttingReview(requestDto, access);

        assertEquals("이미 작성된 리뷰입니다.", result);
    }

    @Test
    void testIsWrited() {
        String access = "token";
        Long orderDetailId = 1L;
        when(reviewClient.isWrited(orderDetailId, access)).thenReturn(ResponseEntity.ok(true));

        boolean result = reviewService.isWrited(access, orderDetailId);

        assertTrue(result);
    }

    @Test
    void testGetReviewInfo() {
        String access = "token";
        Long orderDetailId = 1L;
        ReviewInfoResponseDto reviewInfo = new ReviewInfoResponseDto();
        when(reviewClient.getReviewInfo(orderDetailId, access)).thenReturn(ResponseEntity.ok(reviewInfo));

        ReviewInfoResponseDto result = reviewService.getReviewInfo(access, orderDetailId);

        assertNotNull(result);
    }

    @Test
    void testUpdateReview() {
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto();
        String access = "token";
        when(reviewClient.updateReview(requestDto, access)).thenReturn(ResponseEntity.ok("Updated"));

        String result = reviewService.updateReview(requestDto, access);

        assertEquals("Updated", result);
    }

    @Test
    void testGetReviewScore() {
        Long productId = 1L;
        when(reviewClient.getReviewScore(productId)).thenReturn(ResponseEntity.ok(4.5));

        Double result = reviewService.getReviewScore(productId);

        assertEquals(4.5, result);
    }

    @Test
    void testGetProductReviewPage() {
        int page = 0;
        int size = 10;
        Long productId = 1L;
        Page<ReviewInfoResponseDto> reviewPage = new PageImpl<>(Collections.emptyList());
        when(reviewClient.getReviewProducts(page, size, productId)).thenReturn(ResponseEntity.ok(reviewPage));

        Page<ReviewInfoResponseDto> result = reviewService.getProductReviewPage(page, size, productId);

        assertNotNull(result);
    }
}