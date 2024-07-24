package com.nhnacademy.codequestweb.product.book;

import com.nhnacademy.codequestweb.controller.product.everyone.BookProductController;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookProductControllerTest {
    @InjectMocks
    private BookProductController bookProductController;

    @Mock
    private BookProductService bookProductService;

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    private Cookie accessCookie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookProductController).build();
        accessCookie = new Cookie("access", "test");
    }

    @Test
    void getSingleBookTest1() throws Exception {
        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder()
                .categorySet(new LinkedHashSet<>())
                .build();

        List<ReviewInfoResponseDto> reviewList = Arrays.asList(
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build()
        );

        Page<ReviewInfoResponseDto> reviewPage = new PageImpl<>(reviewList);

        when(bookProductService.getSingleBookInfo(any(), eq(1L))).thenReturn(ResponseEntity.ok(responseDto));
        when(reviewService.getReviewScore(1L)).thenReturn(4.9);
        when(reviewService.getProductReviewPage(0, 10, 1L)).thenReturn(reviewPage);

        mockMvc.perform(get("/product/books/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("averageScore", 4.9))
                .andExpect(model().attribute("orderURL", "/non-client/order"));
    }

    @Test
    void getSingleBookTest2() throws Exception {
        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder()
                .categorySet(new LinkedHashSet<>())
                .build();

        List<ReviewInfoResponseDto> reviewList = Arrays.asList(
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build()
        );

        Page<ReviewInfoResponseDto> reviewPage = new PageImpl<>(reviewList);

        when(bookProductService.getSingleBookInfo(any(), eq(1L))).thenReturn(ResponseEntity.ok(responseDto));
        when(reviewService.getReviewScore(1L)).thenReturn(4.9);
        when(reviewService.getProductReviewPage(0, 10, 1L)).thenReturn(reviewPage);

        mockMvc.perform(get("/product/books/1")
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(model().attribute("averageScore", 4.9))
                .andExpect(model().attribute("orderURL", "/client/order"));
    }

    @Test
    void getSingleBookTest3() throws Exception {
        List<ReviewInfoResponseDto> reviewList = Arrays.asList(
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build()
        );

        Page<ReviewInfoResponseDto> reviewPage = new PageImpl<>(reviewList);

        when(bookProductService.getSingleBookInfo(any(), eq(1L))).thenThrow(FeignException.class);
        when(reviewService.getReviewScore(1L)).thenReturn(4.9);
        when(reviewService.getProductReviewPage(0, 10, 1L)).thenReturn(reviewPage);

        mockMvc.perform(get("/product/books/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/books"))
                .andExpect(flash().attribute("alterMessage","도서 목록 조회 과정에 오류가 발생했습니다."));
    }

    @Test
    void getSingleBookTest4() throws Exception {
        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder()
                .categorySet(new LinkedHashSet<>())
                .build();



        when(bookProductService.getSingleBookInfo(any(), eq(1L))).thenReturn(ResponseEntity.ok(responseDto));
        when(reviewService.getReviewScore(1L)).thenReturn(4.9);
        when(reviewService.getProductReviewPage(0, 10, 1L)).thenThrow(FeignException.class);

        mockMvc.perform(get("/product/books/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("alterMessage", "리뷰 조회 과정에서 오류가 발생했습니다."))
                .andExpect(model().attribute("orderURL", "/non-client/order"));
    }

    @Test
    void getSingleBookTest5() throws Exception {
        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder()
                .categorySet(new LinkedHashSet<>())
                .build();

        List<ReviewInfoResponseDto> reviewList = Arrays.asList(
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build(),
                ReviewInfoResponseDto.builder().build()
        );

        Page<ReviewInfoResponseDto> reviewPage = new PageImpl<>(reviewList);


        when(bookProductService.getSingleBookInfo(any(), eq(1L))).thenReturn(ResponseEntity.ok(responseDto));
        when(reviewService.getReviewScore(1L)).thenThrow(FeignException.class);
        when(reviewService.getProductReviewPage(0, 10, 1L)).thenReturn(reviewPage);

        mockMvc.perform(get("/product/books/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("alterMessage", "리뷰 조회 과정에서 오류가 발생했습니다."))
                .andExpect(model().attribute("orderURL", "/non-client/order"));
    }

    static Stream<Arguments> provideTestParameters() {
        return Stream.of(
                Arguments.of("any sort", "전체 검색"),
                Arguments.of("product.productViewCount", "조회순 검색"),
                Arguments.of("pubDate", "출시순 검색"),
                Arguments.of(null, "전체 검색")
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    void getAllBookPageTest1(String sort, String mainText) throws Exception {
        List<BookProductGetResponseDto> responseDtoList = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );

        Page<BookProductGetResponseDto> responsDtoPage = new PageImpl<>(responseDtoList);

        when(bookProductService.getAllBookPage(any(), eq(1), eq(10), eq(sort),any(), eq(0))).thenReturn(ResponseEntity.ok(responsDtoPage));

        mockMvc.perform(get("/product/books")
                .param("page", "1")
                .param("sort", sort))
                .andExpect(status().isOk())
                .andExpect(model().attribute("mainText", mainText))
                .andExpect(model().attribute("url", "/product/books?"));
    }

    @Test
    void getAllBookPageTest2() throws Exception {
        when(bookProductService.getAllBookPage(any(), eq(1), eq(10), any(), any(), eq(0))).thenThrow(FeignException.class);

        mockMvc.perform(get("/product/books")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage","도서 목록 조회 과정에 오류가 발생했습니다."));
    }

    @Test
    void getNameContainingBookPageTest1() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );
        Page<BookProductGetResponseDto> responsDtoPage = new PageImpl<>(responseDtoList);
        when(bookProductService.getNameContainingBookPage(any(), any(), any(), any(), any(), eq("test"), any()))
                .thenReturn(ResponseEntity.ok(responsDtoPage));

        mockMvc.perform(get("/product/books/containing")
                        .param("title", "test")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("mainText", "제목 검색"))
                .andExpect(model().attribute("url", "/product/books/containing?title=test&"));
    }

    @Test
    void getNameContainingBookPageTest2() throws Exception {
        when(bookProductService.getNameContainingBookPage(any(), any(), any(), any(), any(), eq("test"), any()))
                .thenThrow(FeignException.class);

        mockMvc.perform(get("/product/books/containing")
                        .param("title", "test")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage","도서 목록 조회 과정에 오류가 발생했습니다."))
                .andExpect(model().attributeDoesNotExist("mainText", "url"));
    }

    @Test
    void getBookPageFilterByTagTest1() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );
        Page<BookProductGetResponseDto> responsDtoPage = new PageImpl<>(responseDtoList);

        Set<String> tagNameSet = Set.of("tag1", "tag2");

        when(bookProductService.getBookPageFilterByTag(any(), any(), any(), any(), any(), eq(tagNameSet), any(), any()))
                .thenReturn(ResponseEntity.ok(responsDtoPage));

        mockMvc.perform(get("/product/books/tagFilter")
                        .param("tagName", "tag1")
                        .param("tagName", "tag2")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("mainText", "태그 검색 - [tag1, tag2]"))
                .andExpect(model().attribute("url", "/product/books/tagFilter?tagName=tag1,tag2&"));
    }

    @Test
    void getBookPageFilterByTagTest2() throws Exception {
        Set<String> tagNameSet = Set.of("tag1", "tag2");

        when(bookProductService.getBookPageFilterByTag(any(), any(), any(), any(), any(), eq(tagNameSet), any(), any()))
                .thenThrow(FeignException.class);

        mockMvc.perform(get("/product/books/tagFilter")
                        .param("tagName", "tag1")
                        .param("tagName", "tag2")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage","도서 목록 조회 과정에 오류가 발생했습니다."))
                .andExpect(model().attributeDoesNotExist("mainText", "url"));
    }


    @Test
    void getBookPageFilterByCategoryTest1() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );
        Page<BookProductGetResponseDto> responsDtoPage = new PageImpl<>(responseDtoList);

        when(bookProductService.getBookPageFilterByCategory(any(), any(), any(), any(), any(), eq(1L), any()))
                .thenReturn(ResponseEntity.ok(responsDtoPage));

        mockMvc.perform(get("/product/books/category/1")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("mainText", "카테고리 검색"))
                .andExpect(model().attribute("url", "/product/books/category/1?"));
    }

    @Test
    void getBookPageFilterByCategoryTest2() throws Exception {
        when(bookProductService.getBookPageFilterByCategory(any(), any(), any(), any(), any(), eq(1L), any()))
                .thenThrow(FeignException.class);

        mockMvc.perform(get("/product/books/category/1")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage","도서 목록 조회 과정에 오류가 발생했습니다."))
                .andExpect(model().attributeDoesNotExist("mainText", "url"));
    }


    @Test
    void getLikeBookPageTest1() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = Arrays.asList(
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build(),
                BookProductGetResponseDto.builder().build()
        );
        Page<BookProductGetResponseDto> responsDtoPage = new PageImpl<>(responseDtoList);

        when(bookProductService.getLikeBookPage(any(), any(), any(), any(), any()))
                .thenReturn(ResponseEntity.ok(responsDtoPage));

        mockMvc.perform(get("/product/books/like")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("mainText", "좋아요 목록"))
                .andExpect(model().attribute("url", "/product/books/like?"));
    }

    @Test
    void getLikeBookPageTest2() throws Exception {
        when(bookProductService.getLikeBookPage(any(), any(), any(), any(), any()))
                .thenThrow(FeignException.class);

        mockMvc.perform(get("/product/books/like")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage","도서 목록 조회 과정에 오류가 발생했습니다."))
                .andExpect(model().attributeDoesNotExist("mainText", "url"));
    }
}
