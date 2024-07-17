package com.nhnacademy.codequestweb.response.review;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteReviewResponseDtoTest {

    @Test
    void testBuilder() {
        WriteReviewResponseDto dto = WriteReviewResponseDto.builder()
                .productId(1L)
                .productOrderDetailId(1L)
                .title("Sample Title")
                .author("Sample Author")
                .cover("Sample Cover")
                .build();

        assertEquals(1L, dto.getProductId());
        assertEquals(1L, dto.getProductOrderDetailId());
        assertEquals("Sample Title", dto.getTitle());
        assertEquals("Sample Author", dto.getAuthor());
        assertEquals("Sample Cover", dto.getCover());
    }

    @Test
    void testGetters() {
        WriteReviewResponseDto dto = WriteReviewResponseDto.builder()
                .productId(1L)
                .productOrderDetailId(1L)
                .title("Sample Title1")
                .author("Sample Author")
                .cover("Sample Cover")
                .build();

        assertEquals(1L, dto.getProductId());
        assertEquals(1L, dto.getProductOrderDetailId());
        assertEquals("Sample Title1", dto.getTitle());
        assertEquals("Sample Author", dto.getAuthor());
        assertEquals("Sample Cover", dto.getCover());
    }
}
