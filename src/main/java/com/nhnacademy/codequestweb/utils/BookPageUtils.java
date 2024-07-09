package com.nhnacademy.codequestweb.utils;

import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public class BookPageUtils {

    public BookPageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void setBookPage (ResponseEntity<Page<BookProductGetResponseDto>> response, Integer page, String sort, Boolean desc, Model model) {
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "productList");
        model.addAttribute("productList", response.getBody().getContent());
        page = page == null ? 1 : page;
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
    }
}

