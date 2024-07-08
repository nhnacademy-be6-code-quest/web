package com.nhnacademy.codequestweb.response.order.pack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class PackageInfoResponseDto {
    private long productId;
    private String packageName;
    private long productPriceSales;
}
