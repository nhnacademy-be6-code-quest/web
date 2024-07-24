package com.nhnacademy.codequestweb.response.order.nonclient;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class NonClientOrderGetResponseDto {
    private Long orderId;
    private String orderCode;
    private String orderDatetime;
    private String orderStatus;
    private Long productTotalAmount;
    private Integer shippingFee;
    private Long orderTotalAmount;
    private String designatedDeliveryDate;
    private String deliveryStartDate;
    private String phoneNumber;
    private String deliveryAddress;
    private String nonClientOrderPassword;
    private String nonClientOrdererName;
    private String nonClientOrdererEmail;
    private List<NonClientProductOrderDetailListItem> nonClientProductOrderDetailList;

    @NoArgsConstructor
    @Getter
    public static class NonClientProductOrderDetailListItem{
        private Long productId;
        private String productName;
        private Long productQuantity;
        private Long productSinglePrice;

        private Long optionProductId;
        private String optionProductName;
        private Long optionProductQuantity;
        private Long optionProductSinglePrice;
    }
}
