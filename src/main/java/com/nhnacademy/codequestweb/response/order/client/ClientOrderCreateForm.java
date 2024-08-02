package com.nhnacademy.codequestweb.response.order.client;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ClientOrderCreateForm {

    List<OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트
    Long couponId; // 적용한 쿠폰
    Integer shippingFee; // 배송비
    Long productTotalAmount; // 상품 총 금액(포인트 및 쿠폰 할인 전)
    Long payAmount; // 최종 결제 금액
    Long orderTotalAmount;
    Long couponDiscountAmount; // 쿠폰 할인 금액
    Long usedPointDiscountAmount; // 포인트 사용 금액
    String phoneNumber; // 주문자 핸드폰 번호
    String addressZipCode; // 배송지 우편번호
    String deliveryAddress; // 주소(주소,상세주소)
    Boolean useDesignatedDeliveryDate; // 배송날짜 지정 여부
    String designatedDeliveryDate; // 배송날짜 지정
    Long accumulatePoint; // 예상 적립금
    String orderCode; // 토스 주문 아이디

    public void addOrderDetailDtoItem(OrderDetailDtoItem orderDetailDtoItem){
        if(this.orderDetailDtoItemList == null){
            this.orderDetailDtoItemList = new ArrayList<>();
        }
        this.orderDetailDtoItemList.add(orderDetailDtoItem);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class OrderDetailDtoItem{
        Long productId; // 상품 아이디
        String productName; // 상품 이름
        Long quantity; // 수량
        List<Long> categoryIdList; // 상품의 카테고리
        Long productSinglePrice; // 상품 단품 가격
        Boolean packableProduct; // 포장 가능 상품 여부

        Boolean usePackaging; // 포장 선택 여부
        Long optionProductId; // 옵션 상품 아이디
        String optionProductName; // 옵션 상품 이름
        Long optionProductSinglePrice; // 옵션 상품 단품 가격
        Long optionQuantity = 1L;

    }

}
