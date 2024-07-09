package com.nhnacademy.codequestweb.response.order.nonclient;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class NonClientOrderForm {

    List<OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트
    Integer shippingFee; // 배송비
    Long productTotalAmount; // 상품 총 금액
    Long payAmount; // 최종 결제 금액
    String orderedPersonName; // 비회원 주문자 이름
    String email; // 이메일
    String phoneNumber; // 비회원 주문자 전화번호
    String addressZipCode; // 배송지 우편번호
    String deliveryAddress; // 배송 주소
    String deliveryDetailAddress; // 배송 상세주소
    Boolean useDesignatedDeliveryDate; // 배송날짜 지정 여부
    String designatedDeliveryDate; // 배송날짜 지정
    Integer paymentMethod; // 결제 방식
    String orderPassword; // 주문 비밀번호

    public void addOrderDetailDtoItem(OrderDetailDtoItem orderDetailDtoItem){
        if(this.orderDetailDtoItemList == null){
            this.orderDetailDtoItemList = new ArrayList<>();
        }
        this.orderDetailDtoItemList.add(orderDetailDtoItem);
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class OrderDetailDtoItem{
        Long productId; // 상품 아이디
        String productName; // 상품 이름
        Long quantity; // 수량
        Long bookId; // 상품의 책 아이디 TODO 추후 삭제될 예정
        Long productSinglePrice; // 상품 단품 가격

        Boolean usePackaging; // 포장 선택 여부
        Long optionProductId; // 옵션 상품 아이디
        String optionProductName; // 옵션 상품 이름
        Long optionProductSinglePrice; // 옵션 상품 단품 가격
        Long optionQuantity = 1L;

        @Builder
        public OrderDetailDtoItem(Long productId, String productName, Long quantity, Long bookId, Long productSinglePrice){
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.bookId = bookId;
            this.productSinglePrice = productSinglePrice;
        }
    }

}
