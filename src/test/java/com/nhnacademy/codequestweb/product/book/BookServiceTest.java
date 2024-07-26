package com.nhnacademy.codequestweb.product.book;

import com.nhnacademy.codequestweb.client.product.book.BookProductClient;
import com.nhnacademy.codequestweb.request.product.book_product.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.book_product.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    void roleCheckTest(){
        when(bookProductClient.roleCheck(any())).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = bookService.roleCheck(headers);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookProductClient, times(1)).roleCheck(any());
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

        when(bookProductClient.getBookListForAdmin(any(), eq(1), eq("test"))).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));

        ResponseEntity<Page<AladinBookResponseDto>> responseEntity = bookService.getBookListForAdmin(headers,1, "test");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDtoPage, responseEntity.getBody());
        assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getBookListForAdmin (headers,1, "test");
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
        when(bookProductClient.isbnCheckForAdmin(any(), eq(isbn))).thenReturn(new ResponseEntity<>(null, headers, HttpStatus.OK));
        ResponseEntity<Void> responseEntity = bookService.isbnCheckForAdmin(headers, isbn);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(bookProductClient, times(1)).isbnCheckForAdmin(headers, isbn);
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
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(4, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getNameContainingBookPageByProductState(headers, 1, 10, "sort", true, "title", 0);
    }

    @Test
    void getBookPageFilterByTagTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));
        Set<String> tagSet = Set.of("tag1", "tag2");

        when(bookProductClient.getBookPageFilterByTagAndProductState(headers, 1, 10, "sort", true, tagSet, true, 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getBookPageFilterByTag(headers, 1, 10, "sort", true, tagSet, true, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(5, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getBookPageFilterByTagAndProductState(headers, 1, 10, "sort", true, tagSet, true, 0);
    }

    @Test
    void getBookPageFilterByCategoryTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        Map<String, Page<BookProductGetResponseDto>> responseDtoMap = Map.of("test", responseDtoPage);

        when(bookProductClient.getBookPageFilterByCategory(headers,1,10,"sort",true,1L, 0)).thenReturn(new ResponseEntity<>(responseDtoMap, headers, HttpStatus.OK));
        ResponseEntity<Map<String, Page<BookProductGetResponseDto>>> response = bookService.getBookPageFilterByCategory(headers, 1, 10, "sort", true, 1L, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(6, Objects.requireNonNull(Objects.requireNonNull(response.getBody()).values().stream().findFirst().orElseThrow()).getTotalElements());
        verify(bookProductClient, times(1)).getBookPageFilterByCategory(headers, 1, 10, "sort", true, 1L, 0);
    }

    @Test
    void getLikeBookPageTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        when(bookProductClient.getLikeBookPage(headers, 1, 10, "sort", true, 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getLikeBookPage(headers, 1, 10, "sort", true, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(7, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getLikeBookPage(headers,1,10,"sort",true, 0);
    }

    @Test
    void getSingleBookForAdminTest(){
        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder().build();
        long productId = 1L;
        when(bookProductClient.getSingleBookInfoForAdmin(headers, productId)).thenReturn(new ResponseEntity<>(responseDto, headers, HttpStatus.OK));

        ResponseEntity<BookProductGetResponseDto> responseEntity = bookService.getSingleBookInfoForAdmin(headers, productId);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDto, responseEntity.getBody());
        verify(bookProductClient, times(1)).getSingleBookInfoForAdmin(headers, productId);
    }

    @Test
    void getAllBookPageForAdminTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        when(bookProductClient.getAllBookPageByProductStateForAdmin(headers, 1,  10, "sort", true, 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getAllBookPageForAdmin(headers, 1, 10, "sort", true, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(3, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getAllBookPageByProductStateForAdmin(headers, 1, 10, "sort", true, 0);
    }

    @Test
    void getNameContainingBookPageForAdminTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        when(bookProductClient.getNameContainingBookPageByProductStateForAdmin(headers, 1, 10, "sort", true, "title", 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getNameContainingBookPageForAdmin(headers, 1, 10, "sort", true, "title", 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(4, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getNameContainingBookPageByProductStateForAdmin(headers, 1, 10, "sort", true, "title", 0);
    }

    @Test
    void getBookPageFilterByTagForAdminTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));
        Set<String> tagSet = Set.of("tag1", "tag2");

        when(bookProductClient.getBookPageFilterByTagAndProductStateForAdmin(headers, 1, 10, "sort", true, tagSet, true, 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getBookPageFilterByTagForAdmin(headers, 1, 10, "sort", true, tagSet, true, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(5, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getBookPageFilterByTagAndProductStateForAdmin(headers, 1, 10, "sort", true, tagSet, true, 0);
    }

    @Test
    void getBookPageFilterByCategoryForAdminTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        Map<String, Page<BookProductGetResponseDto>> responseDtoMap = Map.of("test", responseDtoPage);

        when(bookProductClient.getBookPageFilterByCategoryForAdmin(headers,1,10,"sort",true,1L, 0)).thenReturn(new ResponseEntity<>(responseDtoMap, headers, HttpStatus.OK));
        ResponseEntity<Map<String, Page<BookProductGetResponseDto>>> response = bookService.getBookPageFilterByCategoryForAdmin(headers, 1, 10, "sort", true, 1L, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(6, Objects.requireNonNull(Objects.requireNonNull(response.getBody()).values().stream().findFirst().orElseThrow()).getTotalElements());
        verify(bookProductClient, times(1)).getBookPageFilterByCategoryForAdmin(headers, 1, 10, "sort", true, 1L, 0);
    }

    @Test
    void getLikeBookPageForAdminTest(){
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        ));

        when(bookProductClient.getLikeBookPageForAdmin(headers, 1, 10, "sort", true, 0)).thenReturn(new ResponseEntity<>(responseDtoPage, headers, HttpStatus.OK));
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookService.getLikeBookPageForAdmin(headers, 1, 10, "sort", true, 0);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDtoPage, response.getBody());
        assertEquals(7, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(bookProductClient, times(1)).getLikeBookPageForAdmin(headers,1,10,"sort",true, 0);
    }
}
