package com.nhnacademy.codequestweb.response.order.field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductItemDto {
    private long id;
    private String imgPath;
    private String name;
    private long price;
    private long quantity;
}
