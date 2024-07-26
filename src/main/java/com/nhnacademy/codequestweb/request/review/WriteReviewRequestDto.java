package com.nhnacademy.codequestweb.request.review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WriteReviewRequestDto {
    private Long productId;
    private Long productOrderDetailId;
    @Min(1)
    @Max(5)
    private byte reviewScore;
    private String reviewContent;
}
