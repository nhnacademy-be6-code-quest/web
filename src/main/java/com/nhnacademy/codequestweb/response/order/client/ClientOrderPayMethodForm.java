package com.nhnacademy.codequestweb.response.order.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientOrderPayMethodForm {
    private String paymentMethod; // 결제 수단 id
    private Long expectedAccumulatingPoint; // 예상 적립금

    @Builder
    public ClientOrderPayMethodForm(Long expectedAccumulatingPoint){
        this.expectedAccumulatingPoint = expectedAccumulatingPoint;
    }
}
