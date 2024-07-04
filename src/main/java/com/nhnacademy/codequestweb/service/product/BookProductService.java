package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductClient;
import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookProductService {
    private final BookProductClient bookProductClient;

    public ResponseEntity<ProductRegisterResponseDto> saveBook(
            HttpHeaders headers,
            BookProductRegisterRequestDto bookProductRegisterRequestDto) {
        return bookProductClient.saveBook(headers, bookProductRegisterRequestDto);
    }

    public ResponseEntity<Page<AladinBookResponseDto>> getBookList(Integer page, String title) {
        return bookProductClient.getBookList(page, title);
    }

    public ResponseEntity<ProductUpdateResponseDto> updateBook(
            HttpHeaders headers,
            BookProductUpdateRequestDto bookProductUpdateRequestDto) {
        return bookProductClient.updateBook(headers, bookProductUpdateRequestDto);
    }

    public ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(long id) {
        return bookProductClient.getSingleBookInfo(id);
    }


    public ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPage(Integer page, Integer size, String sort, Boolean desc) {
        return bookProductClient.getAllBookPage(page, size, sort, desc);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByTag(Integer page, Integer size, String sort, Boolean desc, Set<String> tagNameSet, Boolean conditionIsAnd) {
        return bookProductClient.getBookPageFilterByTag(page, size, sort, desc, tagNameSet, conditionIsAnd);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByCategory(Integer page, Integer size, String sort, Boolean desc, String categoryName) {
        return bookProductClient.getBookPageFilterByCategory(page, size, sort, desc, categoryName);
    }

    public ResponseEntity<Void> saveBookLike(HttpHeaders headers, ProductLikeRequestDto productLikeRequestDto) {
        return bookProductClient.saveBookProductLike(headers, productLikeRequestDto);
    }
}
