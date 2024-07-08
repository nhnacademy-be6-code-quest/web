package com.nhnacademy.codequestweb.response.order.field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductItemDto {
    private long id;
    //private String imgPath;
    private String name;
    private long price;
    private long quantity;
    private List<Long> categoryList;
}
