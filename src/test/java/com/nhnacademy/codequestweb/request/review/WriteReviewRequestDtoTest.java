package com.nhnacademy.codequestweb.request.review;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteReviewRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto(1L, 2L, (byte) 5, "Great product!");
        assertEquals(1L, dto.getProductId());
        assertEquals(2L, dto.getProductOrderDetailId());
        assertEquals(5, dto.getReviewScore());
        assertEquals("Great product!", dto.getReviewContent());
    }

    @Test
    void getProductId() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setProductId(1L);
        assertEquals(1L, dto.getProductId());
    }

    @Test
    void getProductOrderDetailId() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setProductOrderDetailId(2L);
        assertEquals(2L, dto.getProductOrderDetailId());
    }

    @Test
    void getReviewScore() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setReviewScore((byte) 5);
        assertEquals(5, dto.getReviewScore());
    }

    @Test
    void getReviewContent() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setReviewContent("Great product!");
        assertEquals("Great product!", dto.getReviewContent());
    }

    @Test
    void setProductId() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setProductId(2L);
        assertEquals(2L, dto.getProductId());
    }

    @Test
    void setProductOrderDetailId() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setProductOrderDetailId(3L);
        assertEquals(3L, dto.getProductOrderDetailId());
    }

    @Test
    void setReviewScore() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setReviewScore((byte) 1);
        assertEquals(1, dto.getReviewScore());
    }

    @Test
    void setReviewContent() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto();
        dto.setReviewContent("Great product!1");
        assertEquals("Great product!1", dto.getReviewContent());
    }

    @Test
    void testToString() {
        WriteReviewRequestDto dto = new WriteReviewRequestDto(1L, 2L, (byte) 5, "Great product!");
        String expected = "WriteReviewRequestDto(productId=1, productOrderDetailId=2, reviewScore=5, reviewContent=Great product!)";
        assertEquals(expected, dto.toString());
    }
}