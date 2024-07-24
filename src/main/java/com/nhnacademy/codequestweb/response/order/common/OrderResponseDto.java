package com.nhnacademy.codequestweb.response.order.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class OrderResponseDto {

    private Long orderId;
    private Long clientId;
    private Long couponId;
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
    private Long discountAmountByCoupon;
    private Long discountAmountByPoint;
    private Long accumulatedPoint;
    private String nonClientOrderPassword;
    private String nonClientOrdererName;
    private String nonClientOrdererEmail;
    private List<OrderListItem> orderListItemList;

    @NoArgsConstructor
    @Getter
    public static class OrderListItem{
        private Long productOrderDetailId;

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
