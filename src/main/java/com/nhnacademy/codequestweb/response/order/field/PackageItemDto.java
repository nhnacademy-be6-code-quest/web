package com.nhnacademy.codequestweb.response.order.field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PackageItemDto {
    private long id;
    private String name;
    private long price;
}
