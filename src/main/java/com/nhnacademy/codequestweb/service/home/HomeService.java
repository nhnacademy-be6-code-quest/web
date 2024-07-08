package com.nhnacademy.codequestweb.service.home;

import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {
    private final BookProductService bookProductService;

    public List<BookProductGetResponseDto> getPopularBooks() {
        try {
            return bookProductService.getAllBookPage(null, 1, 20, "product.productViewCount", true, 0)
                    .getBody().getContent();
        } catch (Exception e) {
            return null;
        }
    }

    public List<BookProductGetResponseDto> getNewBooks() {
        try {
            return bookProductService.getAllBookPage(null, 1, 20, "pubDate", true, 0)
                    .getBody().getContent();
        } catch (Exception e) {
            return null;
        }
    }
}
