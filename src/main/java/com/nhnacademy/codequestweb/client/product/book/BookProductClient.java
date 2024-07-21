package com.nhnacademy.codequestweb.client.product.book;

import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "book", url = "http://localhost:8001")
public interface BookProductClient {

        @GetMapping("/api/product/book")
        ResponseEntity<Page<AladinBookResponseDto>> getBookList(
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam("title") String title);

        @PostMapping("/api/product/admin/book/register")
        ResponseEntity<ProductRegisterResponseDto> saveBook(
                @RequestHeader HttpHeaders headers,
                @RequestBody @Valid BookProductRegisterRequestDto bookProductRegisterRequestDto);

        @PutMapping("/api/product/admin/book/update")
        ResponseEntity<ProductUpdateResponseDto> updateBook(
                @RequestHeader HttpHeaders headers,
                @RequestBody @Valid BookProductUpdateRequestDto bookProductUpdateRequestDto);

        @GetMapping("/api/product/book/isbnCheck")
        ResponseEntity<Boolean> isbnCheck(
                @RequestParam("isbn") String isbn
        );


        @GetMapping("/api/product/book/{productId}")
        ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(
                @RequestHeader HttpHeaders headers,
                @PathVariable("productId") long productId);

        @GetMapping("/api/product/books")
        ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPageByProductState(
                @RequestHeader HttpHeaders headers,
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false) String sort,
                @RequestParam(name = "desc", required = false) Boolean desc,
                @RequestParam(name= "productState", required = false) Integer productState
        );

        @GetMapping("/api/product/books/containing")
        ResponseEntity<Page<BookProductGetResponseDto>> getNameContainingBookPageByProductState(
                @RequestHeader HttpHeaders headers,
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc,
                @RequestParam(name = "title")String title,
                @RequestParam(name= "productState", required = false) Integer productState
        );

        @GetMapping("/api/product/books/tagFilter")
        ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByTagAndProductState(
                @RequestHeader HttpHeaders headers,
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc,
                @RequestParam("tagName") Set<String> tagNameSet,
                @RequestParam(value = "isAnd", required = false)Boolean conditionIsAnd,
                @RequestParam(name= "productState", required = false) Integer productState
        );

        @GetMapping("/api/product/books/category/{categoryId}")
        ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByCategory(
                @RequestHeader HttpHeaders headers,
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc,
                @PathVariable("categoryId") Long categoryId,
                @RequestParam(name= "productState", required = false) Integer productState
                );

        @GetMapping("/api/product/client/books/like")
        ResponseEntity<Page<BookProductGetResponseDto>> getLikeBookPage(
                @RequestHeader HttpHeaders headers,
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc);

}
