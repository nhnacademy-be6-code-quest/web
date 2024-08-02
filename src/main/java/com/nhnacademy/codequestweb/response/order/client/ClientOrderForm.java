package com.nhnacademy.codequestweb.response.order.client;

import com.nhnacademy.codequestweb.response.payment.PaymentMethodResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ClientOrderForm {

    // 주문 상품 및 배송지 정보
    List<ClientOrderForm.OrderDetailDtoItem> orderDetailDtoItemList; // 상품-옵션 리스트
    Integer shippingFee; // 회원 배송비
    Long productTotalAmount; // 상품 총 금액
    Long orderTotalAmount; // 주문 총 금액 (상품 총 금액 + 배송비)
    String phoneNumber; // 주문자 핸드폰 번호
    String addressZipCode; // 배송지 우편번호
    String deliveryAddress; // 주소(주소 + 상세주소)
    Boolean useDesignatedDeliveryDate = false; // 배송날짜 지정 여부
    String designatedDeliveryDate; // 배송날짜 지정
    Long totalQuantity;

    // 할인 금액(쿠폰 및 포인트) 정보
    Long payAmount; // 실제 결제할 가격. 쿠폰 할인, 포인트 사용 적용 후 금액!
    Long couponId; // 적용한 쿠폰
    Long couponDiscountAmount = 0L; // 쿠폰 할인 금액
    Long usedPointDiscountAmount = 0L; // 포인트 사용 금액

    // 결제 수단 정보
    String paymentMethod; // 결제 수단 리스트

    public void addOrderDetailDtoItem(ClientOrderForm.OrderDetailDtoItem orderDetailDtoItem){
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
        Long productSinglePrice; // 상품 단품 가격
        Boolean packableProduct; // 포장 가능 상품 여부

        Boolean usePackaging; // 포장 선택 여부
        Long optionProductId; // 옵션 상품 아이디
        String optionProductName; // 옵션 상품 이름
        Long optionProductSinglePrice; // 옵션 상품 단품 가격
        Long optionQuantity = 0L;

        @Builder
        public OrderDetailDtoItem(Long productId, String productName, Long quantity, List<Long> categoryIdList, Boolean packableProduct,Long productSinglePrice){
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.categoryIdList = categoryIdList;
            this.packableProduct = packableProduct;
            this.productSinglePrice = productSinglePrice;
        }
    }

}
