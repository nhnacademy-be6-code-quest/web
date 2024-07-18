package com.nhnacademy.codequestweb.utils;

import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class BookPageUtilsTest {

    @Mock
    private Model model;

    @Mock
    private Page<BookProductGetResponseDto> page;

    @Test
    void testSetBookPage() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        String sort = "title";
        Boolean desc = true;
        Integer pageNum = 2;
        List<BookProductGetResponseDto> content = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );
        when(page.getContent()).thenReturn(content);
        when(page.getTotalPages()).thenReturn(5);

        ResponseEntity<Page<BookProductGetResponseDto>> response = ResponseEntity.ok(page);

        // Act
        BookPageUtils.setBookPage(response, pageNum, sort, desc, model);

        // Assert
        verify(model).addAttribute("sort", sort);
        verify(model).addAttribute("desc", desc);
        verify(model).addAttribute("view", "productList");
        verify(model).addAttribute("productList", content);
        verify(model).addAttribute("page", pageNum);
        verify(model).addAttribute("totalPage", 5);
    }

    @Test
    void testSetBookPageWithNullPage() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        String sort = "title";
        Boolean desc = false;
        Integer pageNum = null;
        List<BookProductGetResponseDto> content = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );
        when(page.getContent()).thenReturn(content);
        when(page.getTotalPages()).thenReturn(3);

        ResponseEntity<Page<BookProductGetResponseDto>> response = ResponseEntity.ok(page);

        // Act
        BookPageUtils.setBookPage(response, pageNum, sort, desc, model);

        // Assert
        verify(model).addAttribute("sort", sort);
        verify(model).addAttribute("desc", desc);
        verify(model).addAttribute("view", "productList");
        verify(model).addAttribute("productList", content);
        verify(model).addAttribute("page", 1);  // Default to 1 when pageNum is null
        verify(model).addAttribute("totalPage", 3);
    }
}