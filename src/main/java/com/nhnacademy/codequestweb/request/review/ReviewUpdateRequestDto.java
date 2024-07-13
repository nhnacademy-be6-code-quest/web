package com.nhnacademy.codequestweb.request.review;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDto {
    private Long reviewId;
    private String reviewContent;
}
