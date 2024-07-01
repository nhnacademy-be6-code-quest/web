package com.nhnacademy.codequestweb.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONPropertyIgnore;

@Builder
@Getter
public class PaymentRequestDto {

//    @NotNull
//    @JsonProperty("amount")
//    long payAmount;
    long amount;

//    @NotNull
//    @JsonProperty("orderId")
//    String tossOrderId;
    String orderId;

    @NotNull
    String paymentKey;
}
