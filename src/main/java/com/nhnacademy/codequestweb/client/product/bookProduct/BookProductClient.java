package com.nhnacademy.codequestweb.client.product.bookProduct;

import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookListResponseDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
        ResponseEntity<Page<AladinBookResponseDto>> getBookList(@RequestParam(value = "page", required = false) Integer page, @RequestParam("title") String title);

        @PostMapping("/api/product/admin/book/register")
        ResponseEntity<ProductRegisterResponseDto> saveBook(
                @RequestHeader HttpHeaders headers,
                @RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto);

        @PutMapping("/api/product/admin/book/update")
        ResponseEntity<ProductUpdateResponseDto> updateBook(
                @RequestHeader HttpHeaders headers,
                @RequestBody BookProductUpdateRequestDto bookProductUpdateRequestDto);

        @GetMapping("/api/product/book/{bookId}")
        ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(@PathVariable("bookId") long bookId);

        @GetMapping("/api/product/books")
        ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPage(
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc
                );

        @GetMapping("/api/product/books/tagFilter")
        ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByTag(
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc,
                @RequestParam("tagName") Set<String> tagNameSet,
                @RequestParam(value = "isAnd", required = false)Boolean conditionIsAnd);


        @GetMapping("/api/product/books/categoryFilter")
        ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByCategory(
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "size", required = false) Integer size,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc,
                @RequestParam("category") String categoryName);

        @PostMapping("/api/product/client/like")
        ResponseEntity<Void> saveBookProductLike(@RequestHeader HttpHeaders headers,
                                                 @RequestBody @Valid ProductLikeRequestDto productLikeRequestDto);
}
