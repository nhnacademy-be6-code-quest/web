package com.nhnacademy.codequestweb.response.coupon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductGetResponseDtoTest {

    @Test
    void testConstructorAndGetters() {
        ProductGetResponseDto dto = new ProductGetResponseDto(
                1L, "Test Product", 1, 10000L, 9000L, "thumbnail.jpg");

        assertEquals(1L, dto.productId());
        assertEquals("Test Product", dto.productName());
        assertEquals(1, dto.productState());
        assertEquals(10000L, dto.productPriceStandard());
        assertEquals(9000L, dto.productPriceSales());
        assertEquals("thumbnail.jpg", dto.productThumbNailImage());
    }

    @Test
    void testBuilder() {
        ProductGetResponseDto dto = ProductGetResponseDto.builder()
                .productId(2L)
                .productName("Builder Product")
                .productState(2)
                .productPriceStandard(20000L)
                .productPriceSales(18000L)
                .productThumbNailImage("builder_thumbnail.jpg")
                .build();

        assertEquals(2L, dto.productId());
        assertEquals("Builder Product", dto.productName());
        assertEquals(2, dto.productState());
        assertEquals(20000L, dto.productPriceStandard());
        assertEquals(18000L, dto.productPriceSales());
        assertEquals("builder_thumbnail.jpg", dto.productThumbNailImage());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductGetResponseDto dto1 = new ProductGetResponseDto(1L, "Product", 1, 10000L, 9000L, "thumb.jpg");
        ProductGetResponseDto dto2 = new ProductGetResponseDto(1L, "Product", 1, 10000L, 9000L, "thumb.jpg");
        ProductGetResponseDto dto3 = new ProductGetResponseDto(2L, "Other", 2, 20000L, 18000L, "other.jpg");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        ProductGetResponseDto dto = new ProductGetResponseDto(1L, "Product", 1, 10000L, 9000L, "thumb.jpg");

        String toString = dto.toString();
        assertTrue(toString.contains("productId=1"));
        assertTrue(toString.contains("productName=Product"));
        assertTrue(toString.contains("productState=1"));
        assertTrue(toString.contains("productPriceStandard=10000"));
        assertTrue(toString.contains("productPriceSales=9000"));
        assertTrue(toString.contains("productThumbNailImage=thumb.jpg"));
    }

    @Test
    void testBuilderWithNullValues() {
        ProductGetResponseDto dto = ProductGetResponseDto.builder()
                .productId(null)
                .productName(null)
                .productThumbNailImage(null)
                .build();

        assertNull(dto.productId());
        assertNull(dto.productName());
        assertEquals(0, dto.productState());
        assertEquals(0L, dto.productPriceStandard());
        assertEquals(0L, dto.productPriceSales());
        assertNull(dto.productThumbNailImage());
    }
}