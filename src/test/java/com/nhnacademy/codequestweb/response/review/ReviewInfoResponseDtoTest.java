package com.nhnacademy.codequestweb.response.review;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReviewInfoResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        ReviewInfoResponseDto dto = new ReviewInfoResponseDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ReviewInfoResponseDto dto = new ReviewInfoResponseDto(
                1L, (byte) 5, "Great product!", now, now, 1L, 1L, 1L, false, "Product Name"
        );

        assertEquals(1L, dto.getReviewId());
        assertEquals((byte) 5, dto.getReviewScore());
        assertEquals("Great product!", dto.getReviewContent());
        assertEquals(now, dto.getReviewRegisterDate());
        assertEquals(now, dto.getReviewLastModifyDate());
        assertEquals(1L, dto.getClientId());
        assertEquals(1L, dto.getProductOrderDetailId());
        assertEquals(1L, dto.getProductId());
        assertFalse(dto.getIsDeleted());
        assertEquals("Product Name", dto.getProductName());
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ReviewInfoResponseDto dto = ReviewInfoResponseDto.builder()
                .reviewId(1L)
                .reviewScore((byte) 5)
                .reviewContent("Great product!")
                .reviewRegisterDate(now)
                .reviewLastModifyDate(now)
                .clientId(1L)
                .productOrderDetailId(1L)
                .productId(1L)
                .isDeleted(false)
                .productName("Product Name")
                .build();

        assertEquals(1L, dto.getReviewId());
        assertEquals((byte) 5, dto.getReviewScore());
        assertEquals("Great product!", dto.getReviewContent());
        assertEquals(now, dto.getReviewRegisterDate());
        assertEquals(now, dto.getReviewLastModifyDate());
        assertEquals(1L, dto.getClientId());
        assertEquals(1L, dto.getProductOrderDetailId());
        assertEquals(1L, dto.getProductId());
        assertFalse(dto.getIsDeleted());
        assertEquals("Product Name", dto.getProductName());
    }

    @Test
    void testGettersAndSetters() {
        ReviewInfoResponseDto dto = new ReviewInfoResponseDto();
        dto.setReviewId(1L);
        dto.setReviewScore((byte) 5);
        dto.setReviewContent("Great product!");
        LocalDateTime now = LocalDateTime.now();
        dto.setReviewRegisterDate(now);
        dto.setReviewLastModifyDate(now);
        dto.setClientId(1L);
        dto.setProductOrderDetailId(1L);
        dto.setProductId(1L);
        dto.setIsDeleted(false);
        dto.setProductName("Product Name");

        assertEquals(1L, dto.getReviewId());
        assertEquals((byte) 5, dto.getReviewScore());
        assertEquals("Great product!", dto.getReviewContent());
        assertEquals(now, dto.getReviewRegisterDate());
        assertEquals(now, dto.getReviewLastModifyDate());
        assertEquals(1L, dto.getClientId());
        assertEquals(1L, dto.getProductOrderDetailId());
        assertEquals(1L, dto.getProductId());
        assertFalse(dto.getIsDeleted());
        assertEquals("Product Name", dto.getProductName());
    }
}
