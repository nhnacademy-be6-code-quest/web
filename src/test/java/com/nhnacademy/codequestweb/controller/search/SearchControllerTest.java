package com.nhnacademy.codequestweb.controller.search;

import com.nhnacademy.codequestweb.response.search.SearchResponseDto;
import com.nhnacademy.codequestweb.service.search.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchControllerTest {

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch() {
        // Given
        String keyword = "test";
        List<SearchResponseDto> mockSearchResults = Arrays.asList(
                SearchResponseDto.builder()
                        .build(),
                SearchResponseDto.builder()
                        .build(),
                SearchResponseDto.builder()
                        .build()
        );
        Page<SearchResponseDto> mockPage = new PageImpl<>(mockSearchResults);

        when(searchService.search(keyword, 0, 10)).thenReturn(mockPage);

        // When
        ResponseEntity<List<String>> response = searchController.search(keyword);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        verify(searchService, times(1)).search(keyword, 0, 10);
    }

    @Test
    void testSearchWithEmptyResult() {
        // Given
        String keyword = "nonexistent";
        Page<SearchResponseDto> mockPage = new PageImpl<>(List.of());

        when(searchService.search(keyword, 0, 10)).thenReturn(mockPage);

        // When
        ResponseEntity<List<String>> response = searchController.search(keyword);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(searchService, times(1)).search(keyword, 0, 10);
    }
}