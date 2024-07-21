package com.nhnacademy.codequestweb.service.search;

import com.nhnacademy.codequestweb.client.search.SearchClient;
import com.nhnacademy.codequestweb.response.search.SearchResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SearchServiceTest {

    @Mock
    private SearchClient searchClient;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch() {
        // 준비
        String keyword = "test";
        int page = 0;
        int size = 10;
        Page<SearchResponseDto> mockPage = new PageImpl<>(Collections.singletonList(new SearchResponseDto()));

        when(searchClient.findAllBookProductDocuments(keyword, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        // 실행
        Page<SearchResponseDto> result = searchService.search(keyword, page, size);

        // 검증
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());

        verify(searchClient, times(1)).findAllBookProductDocuments(keyword, page, size);
    }

    @Test
    void testSearchWithEmptyResult() {
        // 준비
        String keyword = "nonexistent";
        int page = 0;
        int size = 10;
        Page<SearchResponseDto> mockPage = new PageImpl<>(Collections.emptyList());

        when(searchClient.findAllBookProductDocuments(keyword, page, size))
                .thenReturn(ResponseEntity.ok(mockPage));

        // 실행
        Page<SearchResponseDto> result = searchService.search(keyword, page, size);

        // 검증
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(searchClient, times(1)).findAllBookProductDocuments(keyword, page, size);
    }

    @Test
    void testSearchWithNullResponse() {
        // 준비
        String keyword = "test";
        int page = 0;
        int size = 10;

        when(searchClient.findAllBookProductDocuments(keyword, page, size))
                .thenReturn(ResponseEntity.ok(null));

        // 실행
        Page<SearchResponseDto> result = searchService.search(keyword, page, size);

        // 검증
        assertNull(result);

        verify(searchClient, times(1)).findAllBookProductDocuments(keyword, page, size);
    }
}