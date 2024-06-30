package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductClient;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookProductService {
    private final BookProductClient bookProductClient;

    public ResponseEntity<ProductRegisterResponseDto> saveBook(
           @Validated BookProductRegisterRequestDto bookProductRegisterRequestDto) {
        return bookProductClient.saveBook(bookProductRegisterRequestDto);
    }

    public ResponseEntity<Page<AladinBookResponseDto>> getBookList(HttpHeaders headers, Integer page, String title) {
        return bookProductClient.getBookList(headers, page, title);
    }

    public ResponseEntity<ProductUpdateResponseDto> updateBook(BookProductUpdateRequestDto bookProductUpdateRequestDto) {
        return bookProductClient.updateBook(bookProductUpdateRequestDto);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getAllBooks(Integer page, String sort, Boolean desc) {
        return bookProductClient.getAllBookPage(page, sort, desc);
    }

    public ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(long id) {
        return bookProductClient.getSingleBookInfo(id);
    }

    public ResponseEntity<Void> saveBookLike(HttpHeaders headers, long productId) {
        return bookProductClient.saveBookProductLike(headers, productId);
    }
}
