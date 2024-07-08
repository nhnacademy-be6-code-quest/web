package com.nhnacademy.codequestweb.response.review;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteReviewResponseDto {
    private Long productId;
    private Long productOrderDetailId;
    private String title;
    private String author;
    private String cover;
}
