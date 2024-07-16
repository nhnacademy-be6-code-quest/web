package com.nhnacademy.codequestweb.request.shipping;

import lombok.Builder;

@Builder
public record AdminShippingPolicyPutRequestDto (
        String description,
        int shippingFee,
        int minPurchaseAmount,
        String shippingPolicyType
)
{
}