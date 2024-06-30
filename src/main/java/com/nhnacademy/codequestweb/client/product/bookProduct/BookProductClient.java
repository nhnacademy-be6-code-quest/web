package com.nhnacademy.codequestweb.client.product.bookProduct;

import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookListResponseDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import jakarta.validation.constraints.Min;
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

//@FeignClient(name = "book", url = "http://10.220.222.13:8001")
@FeignClient(name = "book", url = "http://localhost:8004")
public interface BookProductClient {

        @GetMapping("/api/product/admin/book")
        ResponseEntity<Page<AladinBookResponseDto>> getBookList(@RequestHeader HttpHeaders headers, @RequestParam(value = "page", required = false) Integer page, @RequestParam("title") String title);

        @PostMapping("/api/product/admin/book/register")
        ResponseEntity<ProductRegisterResponseDto> saveBook(@RequestBody BookProductRegisterRequestDto bookProductRegisterRequestDto);

        @PutMapping("/api/product/admin/book/update")
        ResponseEntity<ProductUpdateResponseDto> updateBook(@RequestBody BookProductUpdateRequestDto bookProductUpdateRequestDto);

        @GetMapping("/api/product/book/{bookId}")
        ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(@PathVariable("bookId") long bookId);

        @GetMapping("/api/product/books")
        ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPage(
                @RequestParam(value = "page", required = false) Integer page,
                @RequestParam(name = "sort", required = false)String sort,
                @RequestParam(name = "desc", required = false)Boolean desc
                );

        @PostMapping("/api/product/client/{productId}/like")
        ResponseEntity<Void> saveBookProductLike(@RequestHeader HttpHeaders headers, @Min(1)@PathVariable("productId") long bookId);
}
