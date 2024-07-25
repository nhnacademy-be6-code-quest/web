package com.nhnacademy.codequestweb.response.search;

import com.nhnacademy.codequestweb.response.product.product_category.ProductCategory;
import com.nhnacademy.codequestweb.response.product.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SearchResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        SearchResponseDto dto = new SearchResponseDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        Set<ProductCategory> categorySet = Collections.singleton(new ProductCategory(1L, "a", null));
        Set<Tag> tagSet = Collections.singleton(new Tag(1L, "a"));

        SearchResponseDto dto = new SearchResponseDto(
                1L, "cover.jpg", true, "Product Description", "Product Name",
                1, 100L, 200L, 150L, 50L,
                10L, "Book Title", "Publisher", "Author", "ISBN", "ISBN13",
                categorySet, tagSet, true
        );

        assertEquals(1L, dto.getProductId());
        assertEquals("cover.jpg", dto.getCover());
        assertTrue(dto.isPackable());
        assertEquals("Product Description", dto.getProductDescription());
        assertEquals("Product Name", dto.getProductName());
        assertEquals(1, dto.getProductState());
        assertEquals(100L, dto.getProductViewCount());
        assertEquals(200L, dto.getProductPriceStandard());
        assertEquals(150L, dto.getProductPriceSales());
        assertEquals(50L, dto.getProductInventory());
        assertEquals(10L, dto.getBookId());
        assertEquals("Book Title", dto.getTitle());
        assertEquals("Publisher", dto.getPublisher());
        assertEquals("Author", dto.getAuthor());
        assertEquals("ISBN", dto.getIsbn());
        assertEquals("ISBN13", dto.getIsbn13());
        assertEquals(categorySet, dto.getCategorySet());
        assertEquals(tagSet, dto.getTagSet());
        assertTrue(dto.isHasLike());
    }

    @Test
    void testBuilder() {
        Set<ProductCategory> categorySet = Collections.singleton(new ProductCategory(1L, "a", null));
        Set<Tag> tagSet = Collections.singleton(new Tag(1L, "a"));

        SearchResponseDto dto = SearchResponseDto.builder()
                .productId(1L)
                .cover("cover.jpg")
                .packable(true)
                .productDescription("Product Description")
                .productName("Product Name")
                .productState(1)
                .productViewCount(100L)
                .productPriceStandard(200L)
                .productPriceSales(150L)
                .productInventory(50L)
                .bookId(10L)
                .title("Book Title")
                .publisher("Publisher")
                .author("Author")
                .isbn("ISBN")
                .isbn13("ISBN13")
                .categorySet(categorySet)
                .tagSet(tagSet)
                .hasLike(true)
                .build();

        assertEquals(1L, dto.getProductId());
        assertEquals("cover.jpg", dto.getCover());
        assertTrue(dto.isPackable());
        assertEquals("Product Description", dto.getProductDescription());
        assertEquals("Product Name", dto.getProductName());
        assertEquals(1, dto.getProductState());
        assertEquals(100L, dto.getProductViewCount());
        assertEquals(200L, dto.getProductPriceStandard());
        assertEquals(150L, dto.getProductPriceSales());
        assertEquals(50L, dto.getProductInventory());
        assertEquals(10L, dto.getBookId());
        assertEquals("Book Title", dto.getTitle());
        assertEquals("Publisher", dto.getPublisher());
        assertEquals("Author", dto.getAuthor());
        assertEquals("ISBN", dto.getIsbn());
        assertEquals("ISBN13", dto.getIsbn13());
        assertEquals(categorySet, dto.getCategorySet());
        assertEquals(tagSet, dto.getTagSet());
        assertTrue(dto.isHasLike());
    }

    @Test
    void testGettersAndSetters() {
        SearchResponseDto dto = new SearchResponseDto();
        dto.setProductId(1L);
        dto.setCover("cover.jpg");
        dto.setPackable(true);
        dto.setProductDescription("Product Description");
        dto.setProductName("Product Name");
        dto.setProductState(1);
        dto.setProductViewCount(100L);
        dto.setProductPriceStandard(200L);
        dto.setProductPriceSales(150L);
        dto.setProductInventory(50L);
        dto.setBookId(10L);
        dto.setTitle("Book Title");
        dto.setPublisher("Publisher");
        dto.setAuthor("Author");
        dto.setIsbn("ISBN");
        dto.setIsbn13("ISBN13");

        Set<ProductCategory> categorySet = Collections.singleton(new ProductCategory(1L, "a", null));
        Set<Tag> tagSet = Collections.singleton(new Tag(1L, "a"));

        dto.setCategorySet(categorySet);
        dto.setTagSet(tagSet);
        dto.setHasLike(true);

        assertEquals(1L, dto.getProductId());
        assertEquals("cover.jpg", dto.getCover());
        assertTrue(dto.isPackable());
        assertEquals("Product Description", dto.getProductDescription());
        assertEquals("Product Name", dto.getProductName());
        assertEquals(1, dto.getProductState());
        assertEquals(100L, dto.getProductViewCount());
        assertEquals(200L, dto.getProductPriceStandard());
        assertEquals(150L, dto.getProductPriceSales());
        assertEquals(50L, dto.getProductInventory());
        assertEquals(10L, dto.getBookId());
        assertEquals("Book Title", dto.getTitle());
        assertEquals("Publisher", dto.getPublisher());
        assertEquals("Author", dto.getAuthor());
        assertEquals("ISBN", dto.getIsbn());
        assertEquals("ISBN13", dto.getIsbn13());
        assertEquals(categorySet, dto.getCategorySet());
        assertEquals(tagSet, dto.getTagSet());
        assertTrue(dto.isHasLike());
    }
}
