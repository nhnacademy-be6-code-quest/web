package com.nhnacademy.codequestweb.product.book;

import com.nhnacademy.codequestweb.client.product.book.BookProductClient;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookProductService bookService;

    @Mock
    private BookProductClient bookProductClient;

    private HttpHeaders headers;

    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        testTime = LocalDateTime.of(2000, 12, 20, 10, 50);
    }
    @Test
    void saveBookTest(){
        BookProductRegisterRequestDto requestDto = BookProductRegisterRequestDto.builder()
                        .build();
        ProductRegisterResponseDto responseDto = new ProductRegisterResponseDto(1L, testTime);
        when(bookProductClient.saveBook(headers, requestDto)).thenReturn(new ResponseEntity<>(responseDto, headers, HttpStatus.CREATED));

        ResponseEntity<ProductRegisterResponseDto> responseEntity = bookService.saveBook(headers, requestDto);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(responseDto, responseEntity.getBody());
        verify(bookProductClient, times(1)).saveBook(headers, requestDto);
    }

    @Test
    void getBookListTest(){
        Page<AladinBookResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                AladinBookResponseDto.builder().build(),
                AladinBookResponseDto.builder().build()
        ));

        when(bookProductClient.getBookList(1, "test")).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));

        ResponseEntity<Page<AladinBookResponseDto>> responseEntity = bookService.getBookList(1, "test");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDtoPage, responseEntity.getBody());
        assertEquals(3, Objects.requireNonNull(responseEntity.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getBookList(1, "test");
    }

    @Test
    void updateBookTest(){
        BookProductUpdateRequestDto requestDto = BookProductUpdateRequestDto.builder().build();
        ProductUpdateResponseDto responseDto = new ProductUpdateResponseDto(testTime);
        when(bookProductClient.updateBook(headers, requestDto)).thenReturn(new ResponseEntity<>(responseDto, headers, HttpStatus.OK));

        ResponseEntity<ProductUpdateResponseDto> responseEntity = bookService.updateBook(headers, requestDto);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDto, responseEntity.getBody());
        verify(bookProductClient, times(1)).updateBook(headers, requestDto);
    }

    @Test
    void isbnCheckTest(){
        String isbn = "isbn";
        when(bookProductClient.isbnCheck(isbn)).thenReturn(new ResponseEntity<>(true, headers, HttpStatus.OK));
        ResponseEntity<Boolean> responseEntity = bookService.isbnCheck(isbn);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, responseEntity.getBody());
        verify(bookProductClient, times(1)).isbnCheck(isbn);
    }

    @Test
    void getSingleBookTest(){
        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder().build();
        long productId = 1L;
        when(bookProductClient.getSingleBookInfo(headers, productId)).thenReturn(new ResponseEntity<>(responseDto, headers, HttpStatus.OK));

        ResponseEntity<BookProductGetResponseDto> responseEntity = bookService.getSingleBookInfo(headers, productId);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDto, responseEntity.getBody());
        verify(bookProductClient, times(1)).getSingleBookInfo(headers, productId);
    }

    @Test
    void getAllBookPageTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        when(bookProductClient.getAllBookPageByProductState(headers, 1,  10, "sort", true, 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getAllBookPage(headers, 1, 10, "sort", true, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(3, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getAllBookPageByProductState(headers, 1, 10, "sort", true, 0);
    }

    @Test
    void getNameContainingBookPageTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        when(bookProductClient.getNameContainingBookPageByProductState(headers, 1, 10, "sort", true, "title", 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getNameContainingBookPage(headers, 1, 10, "sort", true, "title", 0);
    }
}
