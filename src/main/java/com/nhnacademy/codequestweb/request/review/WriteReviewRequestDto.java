package com.nhnacademy.codequestweb.request.review;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WriteReviewRequestDto {
    private Long productId;
    private Long productOrderDetailId;
    private byte reviewScore;
    private String reviewContent;
}
