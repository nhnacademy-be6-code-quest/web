package com.nhnacademy.codequestweb.response.product.productCategory;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"productCategoryId"})
public class CategoryNodeResponseDto implements Comparable<CategoryNodeResponseDto> {
    private Long productCategoryId;
    private Integer nodeLevel;
    private String categoryName;
    private List<CategoryNodeResponseDto> children;

    @Override
    public int compareTo(CategoryNodeResponseDto o) {
        return this.productCategoryId.compareTo(o.productCategoryId);
    }
}
