package com.nhnacademy.codequestweb.request.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PhotoReviewRequestDTO {

    @Min(1)
    @Max(5)
    @NotNull(message = "평가점수는 필수 입력 항목입니다.")
    private byte score;

    @Size(min = 1, max = 1000)
    @NotBlank(message = "리뷰내용은 필수 입력 항목입니다.")
    private String content;

    @NotNull
    private Long clientId;

    @NotNull
    private Long orderDetailId;

    @NotNull
    private Long productId;

    private List<String> photoUrls;

    private List<String> existingPhotoUrls; // 기존 사진 URL 필드 추가

}
