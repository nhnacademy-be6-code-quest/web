package com.nhnacademy.codequestweb.response.order.nonclient;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * FindNonClientOrderIdInfoResponseDto : 비회원이 주문 아이디 찾기를 시도했을 때, 결과로 나오는 응답 dto
 * @author 박희원(bakhuiwon326)
 * @version 1.0
 **/

@Getter
@NoArgsConstructor
public class FindNonClientOrderIdInfoResponseDto{
    long orderId;
    LocalDateTime orderDateTime;
}
