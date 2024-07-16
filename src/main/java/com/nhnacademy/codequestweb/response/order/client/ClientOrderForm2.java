package com.nhnacademy.codequestweb.response.order.client;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ClientOrderForm2 {
    List<ClientOrderForm2.OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트
    Integer shippingFee; // 배송비
    Long productTotalAmount; // 상품 총 금액
    String orderedPersonName; // 주문자 이름
    String phoneNumber; // 주문자 핸드폰 번호
    String addressNickname; // 배송지 별칭
    String addressZipCode; // 배송지 우편번호
    String deliveryAddress; // 주소(주소,상세주소)
    Boolean useDesignatedDeliveryDate; // 배송날짜 지정 여부
    String designatedDeliveryDate; // 배송날짜 지정
    Long totalQuantity; // 총 수량

    @Builder
    public ClientOrderForm2(String orderedPersonName){
        this.orderedPersonName = orderedPersonName;
    }

    public void updateTotalQuantity(Long totalQuantity){
        this.totalQuantity = totalQuantity;
    }

    public void addOrderDetailDtoItem(ClientOrderForm2.OrderDetailDtoItem orderDetailDtoItem){
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
        List<Long> categoryIdList; // 상품의 카테고리
        Long bookId; // 상품의 책 아이디 TODO 추후 삭제될 예정
        Long productSinglePrice; // 상품 단품 가격
        Boolean packableProduct; // 포장 가능 상품 여부

        Boolean usePackaging; // 포장 선택 여부
        Long optionProductId; // 옵션 상품 아이디
        String optionProductName; // 옵션 상품 이름
        Long optionProductSinglePrice; // 옵션 상품 단품 가격
        Long optionQuantity = 1L;

        @Builder
        public OrderDetailDtoItem(Long productId, String productName, Long quantity, List<Long> categoryIdList, Boolean packableProduct, Long bookId, Long productSinglePrice){
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.categoryIdList = categoryIdList;
            this.packableProduct = packableProduct;
            this.bookId = bookId;
            this.productSinglePrice = productSinglePrice;
        }
    }

}
