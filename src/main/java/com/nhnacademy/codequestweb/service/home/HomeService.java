package com.nhnacademy.codequestweb.service.home;

import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {
    private final BookProductService bookProductService;

    public List<BookProductGetResponseDto> getPopularBooks(HttpHeaders headers) {
        try {
            return bookProductService.getAllBookPage(headers, 1, 20, "product.productViewCount", true, 0)
                    .getBody().getContent();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<BookProductGetResponseDto> getNewBooks(HttpHeaders headers) {
        try {
            return bookProductService.getAllBookPage(headers, 1, 20, "pubDate", true, 0)
                    .getBody().getContent();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<BookProductGetResponseDto> getAlmostSoldOutBooks(HttpHeaders headers) {
        try {
            return bookProductService.getAllBookPage(headers, 1, 20, "product.productInventory", false, 0)
                    .getBody().getContent();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
