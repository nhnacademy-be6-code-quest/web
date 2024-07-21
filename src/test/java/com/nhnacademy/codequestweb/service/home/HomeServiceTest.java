package com.nhnacademy.codequestweb.service.home;

import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HomeServiceTest {

    @Mock
    private BookProductService bookProductService;

    @InjectMocks
    private HomeService homeService;

    private HttpHeaders headers;
    private List<BookProductGetResponseDto> mockBooks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headers = new HttpHeaders();
        mockBooks = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );
    }

    @Test
    void testGetPopularBooks() {
        Page<BookProductGetResponseDto> mockPage = new PageImpl<>(mockBooks);
        when(bookProductService.getAllBookPage(headers, 1, 20, "product.productViewCount", true, 0))
                .thenReturn(ResponseEntity.ok(mockPage));

        List<BookProductGetResponseDto> result = homeService.getPopularBooks(headers);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookProductService).getAllBookPage(headers, 1, 20, "product.productViewCount", true, 0);
    }

    @Test
    void testGetNewBooks() {
        Page<BookProductGetResponseDto> mockPage = new PageImpl<>(mockBooks);
        when(bookProductService.getAllBookPage(headers, 1, 20, "pubDate", true, 0))
                .thenReturn(ResponseEntity.ok(mockPage));

        List<BookProductGetResponseDto> result = homeService.getNewBooks(headers);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookProductService).getAllBookPage(headers, 1, 20, "pubDate", true, 0);
    }

    @Test
    void testGetAlmostSoldOutBooks() {
        Page<BookProductGetResponseDto> mockPage = new PageImpl<>(mockBooks);
        when(bookProductService.getAllBookPage(headers, 1, 20, "product.productInventory", false, 0))
                .thenReturn(ResponseEntity.ok(mockPage));

        List<BookProductGetResponseDto> result = homeService.getAlmostSoldOutBooks(headers);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookProductService).getAllBookPage(headers, 1, 20, "product.productInventory", false, 0);
    }

    @Test
    void testGetPopularBooksWithException() {
        when(bookProductService.getAllBookPage(any(), anyInt(), anyInt(), anyString(), anyBoolean(), anyInt()))
                .thenThrow(new RuntimeException("Test exception"));

        List<BookProductGetResponseDto> result = homeService.getPopularBooks(headers);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetNewBooksWithException() {
        when(bookProductService.getAllBookPage(any(), anyInt(), anyInt(), anyString(), anyBoolean(), anyInt()))
                .thenThrow(new RuntimeException("Test exception"));

        List<BookProductGetResponseDto> result = homeService.getNewBooks(headers);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAlmostSoldOutBooksWithException() {
        when(bookProductService.getAllBookPage(any(), anyInt(), anyInt(), anyString(), anyBoolean(), anyInt()))
                .thenThrow(new RuntimeException("Test exception"));

        List<BookProductGetResponseDto> result = homeService.getAlmostSoldOutBooks(headers);

        assertTrue(result.isEmpty());
    }
}