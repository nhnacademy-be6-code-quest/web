package com.nhnacademy.codequestweb.request.review;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewUpdateRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto(1L, "Updated content");
        assertEquals(1L, dto.getReviewId());
        assertEquals("Updated content", dto.getReviewContent());
    }

    @Test
    void testBuilder() {
        ReviewUpdateRequestDto dto = ReviewUpdateRequestDto.builder()
                .reviewId(1L)
                .reviewContent("Updated content")
                .build();

        assertEquals(1L, dto.getReviewId());
        assertEquals("Updated content", dto.getReviewContent());
    }

    @Test
    void getReviewId() {
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto();
        dto.setReviewId(1L);
        assertEquals(1L, dto.getReviewId());
    }

    @Test
    void getReviewContent() {
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto();
        dto.setReviewContent("Updated content");
        assertEquals("Updated content", dto.getReviewContent());
    }

    @Test
    void setReviewId() {
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto();
        dto.setReviewId(2L);
        assertEquals(2L, dto.getReviewId());
    }

    @Test
    void setReviewContent() {
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto();
        dto.setReviewContent("Updated content1");
        assertEquals("Updated content1", dto.getReviewContent());
    }
}
