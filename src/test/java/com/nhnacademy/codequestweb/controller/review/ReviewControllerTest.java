package com.nhnacademy.codequestweb.controller.review;

import com.nhnacademy.codequestweb.request.review.ReviewUpdateRequestDto;
import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWriteReview() {
        Long orderId = 1L;
        Long orderDetailId = 2L;

        when(reviewService.isWrited("token", orderDetailId)).thenReturn(false);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "token")});
        when(reviewService.writeReview(orderId, orderDetailId, "token")).thenReturn(WriteReviewResponseDto.builder().build());

        String viewName = reviewController.writeReview(orderId, orderDetailId, request, redirectAttributes);

        assertEquals("index", viewName);
        verify(reviewService).isWrited("token", orderDetailId);
        verify(reviewService).writeReview(orderId, orderDetailId, "token");
    }

    @Test
    void testWriteReviewAlreadyWritten() {
        Long orderId = 1L;
        Long orderDetailId = 2L;

        when(reviewService.isWrited("token", orderDetailId)).thenReturn(true);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "token")});

        String viewName = reviewController.writeReview(orderId, orderDetailId, request, redirectAttributes);

        assertEquals("redirect:/", viewName);
        verify(redirectAttributes).addFlashAttribute("alterMessage", "이미 작성된 리뷰입니다.");
    }

    @Test
    void testWriteReviewPost() {
        WriteReviewRequestDto writeReviewRequestDto = new WriteReviewRequestDto();
        writeReviewRequestDto.setProductId(1L);

        when(reviewService.posttingReview(writeReviewRequestDto, "token")).thenReturn("Success");
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "token")});

        ResponseEntity<Map<String, String>> response = reviewController.writeReviewPost(writeReviewRequestDto, request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().get("message"));
        assertEquals("/product/books/1", response.getBody().get("redirectUrl"));
    }

    @Test
    void testReviewDetail() {
        Long reviewId = 1L;

        ReviewInfoResponseDto reviewInfo = new ReviewInfoResponseDto();
        reviewInfo.setProductId(2L);
        BookProductGetResponseDto productInfo = BookProductGetResponseDto.builder().build();

        when(reviewService.getReviewInfo("token", reviewId)).thenReturn(reviewInfo);
        when(reviewService.getProductInfo(2L)).thenReturn(productInfo);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "token")});

        String viewName = reviewController.reviewDetail(reviewId, request);

        assertEquals("index", viewName);
        verify(reviewService).getReviewInfo("token", reviewId);
        verify(reviewService).getProductInfo(2L);
    }

    @Test
    void testUpdateReview() {
        ReviewUpdateRequestDto reviewUpdateRequestDto = new ReviewUpdateRequestDto();

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access", "token")});
        when(reviewService.updateReview(reviewUpdateRequestDto, "token")).thenReturn("Updated");

        ResponseEntity<String> response = reviewController.updateReview(reviewUpdateRequestDto, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated", response.getBody());
    }

    @Test
    void testHandleBadRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String viewName = reviewController.handleBadRequest(request);

        assertEquals("redirect:/", viewName);
    }
}