package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductClient;
import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductStateUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryDecreaseRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryIncreaseRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventorySetRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import java.util.List;
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

    public ResponseEntity<ProductUpdateResponseDto> updateBookState(
            HttpHeaders headers,
            ProductStateUpdateRequestDto productStateUpdateRequestDto){
        return bookProductClient.updateBookState(headers, productStateUpdateRequestDto);
    }

    public ResponseEntity<BookProductGetResponseDto> getSingleBookInfo(
            HttpHeaders headers,
            long productId) {
        return bookProductClient.getSingleBookInfo(headers, productId);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getAllBookPage(
            HttpHeaders headers,
            Integer page, Integer size, String sort, Boolean desc,
            Integer productState) {
        return bookProductClient.getAllBookPageByProductState(headers, page, size, sort, desc, productState);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getNameContainingBookPage(
            HttpHeaders headers,
            Integer page, Integer size, String sort, Boolean desc,
            String title, Integer productState) {
        return bookProductClient.getNameContainingBookPageByProductState(headers, page, size, sort, desc, title, productState);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByTag(
            HttpHeaders headers,
            Integer page, Integer size, String sort, Boolean desc,
            Set<String> tagNameSet, Boolean conditionIsAnd, Integer productState) {
        return bookProductClient.getBookPageFilterByTagAndProductState(headers, page, size, sort, desc, tagNameSet, conditionIsAnd, productState);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getBookPageFilterByCategory(
            HttpHeaders headers,
            Integer page, Integer size, String sort, Boolean desc,
            Long categoryId, Integer productState) {
        return bookProductClient.getBookPageFilterByCategory(headers, page, size, sort, desc, categoryId, productState);
    }

    public ResponseEntity<Page<BookProductGetResponseDto>> getLikeBookPage(
            HttpHeaders headers,
            Integer page, Integer size, String sort, Boolean desc) {
        return bookProductClient.getLikeBookPage(headers, page, size, sort, desc);
    }

    public ResponseEntity<Void> saveBookLike(HttpHeaders headers, ProductLikeRequestDto productLikeRequestDto) {
        return bookProductClient.saveBookProductLike(headers, productLikeRequestDto);
    }

    public ResponseEntity<Void> deleteBookLike(HttpHeaders headers, Long productId) {
        return bookProductClient.deleteBookProductLike(headers, productId);
    }

    public ResponseEntity<Void> decreaseBookInventory(List<InventoryDecreaseRequestDto> requestDtoList) {
        return bookProductClient.decreaseProductInventory(requestDtoList);
    }

    public ResponseEntity<Void> increaseBookInventory(
            HttpHeaders headers,
            InventoryIncreaseRequestDto requestDto) {
        return bookProductClient.increaseProductInventory(headers, requestDto);
    }

    public ResponseEntity<Void> setBookInventory(
            HttpHeaders headers,
            InventorySetRequestDto requestDto) {
        return bookProductClient.setProductInventory(headers, requestDto);
    }
}
