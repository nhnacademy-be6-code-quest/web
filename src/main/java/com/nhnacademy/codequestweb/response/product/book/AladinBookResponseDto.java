package com.nhnacademy.codequestweb.response.product.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AladinBookResponseDto {
    private String title;
    private String cover;
    private String author;
    private String publisher;
    private String priceStandard;
    private String pubDate;
    private String priceSales;
    private String discount;
    private String isbn;
    private String isbn13;
}
