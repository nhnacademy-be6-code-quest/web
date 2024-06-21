package com.nhnacademy.codequestweb.request.bookProduct;

import java.time.LocalDate;
import java.util.List;

public record BookProductRegisterRequestDto(
        String title,
        String publisher,
        String author,
        LocalDate pubDate,
        String isbn,
        String isbn13,

        String cover,
        boolean packable,
        String productDescription,
        long productPriceStandard,
        long productPriceSales,
        long productInventory,
        List<String> categories,
        List<String> tags
) {}
