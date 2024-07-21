package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.payment.*;
import com.nhnacademy.codequestweb.request.payment.*;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto.ProductOrderDetailOptionRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto;
import com.nhnacademy.codequestweb.request.point.PointRewardOrderRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryDecreaseRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService /*implements PaymentService*/ {

    private final PaymentClient paymentClient;
    private final PaymentOrderClient paymentOrderClient;
    private final PaymentCouponClient paymentCouponClient;
    private final PaymentPointClient paymentPointClient;
    private final PaymentProductClient paymentProductClient;
    private final OrderClient orderClient;
    private final PaymentClientClient paymentClientClient;

    public void savePayment(HttpHeaders headers, TossPaymentsResponseDto tossPaymentsResponseDto) {
        log.info("결제 및 주문 생성 시도");
        paymentClient.savePayment(headers, tossPaymentsResponseDto);
    }

    public boolean isValidTossPayment(PaymentOrderApproveRequestDto paymentOrderApproveRequestDto,
        String tossOrderId, long amount) {
        return paymentOrderApproveRequestDto != null
            && paymentOrderApproveRequestDto.getTossOrderId().equals(tossOrderId)
            && paymentOrderApproveRequestDto.getOrderTotalAmount()
            - paymentOrderApproveRequestDto.getDiscountAmountByCoupon()
            - paymentOrderApproveRequestDto.getDiscountAmountByPoint() == amount;
    }

    public TossPaymentsResponseDto approvePayment(HttpHeaders headers, String tossOrderId, long amount,
        String paymentKey) {
        TossApprovePaymentRequest tossApprovePaymentRequest = new TossApprovePaymentRequest();
        tossApprovePaymentRequest.setPaymentKey(paymentKey);
        tossApprovePaymentRequest.setAmount(amount);
        tossApprovePaymentRequest.setOrderId(tossOrderId);
        log.info("결제 승인 시도");
        return paymentClient.approvePayment(headers, tossApprovePaymentRequest).getBody();
    }

    public PaymentOrderShowRequestDto findPaymentOrderShowRequestDtoByOrderId(HttpHeaders headers, String tossOrderId) {
        return orderClient.getPaymentOrderShowRequestDto(headers, tossOrderId).getBody();
    }

    public PaymentOrderApproveRequestDto findPaymentOrderApproveRequestDtoByOrderId(
        HttpHeaders headers, String tossOrderId) {
        return orderClient.getPaymentOrderApproveRequestDto(headers, tossOrderId).getBody();
    }

    public ResponseEntity<String> useCoupon(
        HttpHeaders headers, PaymentCompletedCouponRequestDto paymentCompletedCouponRequestDto) {
        return paymentCouponClient.paymentUsedCoupon(headers, paymentCompletedCouponRequestDto);
    }

    public ResponseEntity<String> usePaymentPoint(
        PaymentUsePointRequestDto paymentUsePointRequestDto, HttpHeaders httpHeaders) {
        return paymentPointClient.usePaymentPoint(paymentUsePointRequestDto, httpHeaders);
    }

    public ResponseEntity<String> accumulatePoint(HttpHeaders httpHeaders, Long amount) {
        PointRewardOrderRequestDto pointRewardOrderRequestDto = new PointRewardOrderRequestDto();
        pointRewardOrderRequestDto.setAccumulatedPoint(amount);
        return paymentPointClient.rewardOrderPoint(httpHeaders, pointRewardOrderRequestDto);
    }
    public void updateGrade(long clientId){
        UserUpdateGradeRequestDto userUpdateGradeRequestDto = new UserUpdateGradeRequestDto(clientId);
        paymentClient.updateUser(userUpdateGradeRequestDto);
    }

    public void decreaseProductInventory(
        List<ProductOrderDetailRequestDto> productOrderDetailRequestDtoList) {
        List<InventoryDecreaseRequestDto> inventoryDecreaseRequestDtoList = new ArrayList<>();

        for (PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto productOrderDetailRequestDto : productOrderDetailRequestDtoList) {
            // 1) 상품을 재고 감소에 넣기
            InventoryDecreaseRequestDto inventoryDecreaseRequestDto = new InventoryDecreaseRequestDto(
                productOrderDetailRequestDto.getProductId(),
                productOrderDetailRequestDto.getQuantity());
            inventoryDecreaseRequestDtoList.add(inventoryDecreaseRequestDto);

            // 2) 상품이 가지고 있는 옵션 상품을 재고에 넣기
            for (ProductOrderDetailOptionRequestDto productOrderDetailOptionRequestDto : productOrderDetailRequestDto.getProductOrderDetailOptionRequestDtoList()) {
                InventoryDecreaseRequestDto inventoryDecreaseRequestDto1 = new InventoryDecreaseRequestDto(
                    productOrderDetailOptionRequestDto.getProductId(),
                    productOrderDetailOptionRequestDto.getOptionProductQuantity());
                inventoryDecreaseRequestDtoList.add(inventoryDecreaseRequestDto1);
            }
        }
        paymentProductClient.decreaseProductInventory(inventoryDecreaseRequestDtoList);
    }

    public ResponseEntity<String> changeOrderStatusCompletePayment(Long orderId, String status) {
        return paymentOrderClient.updateOrderStatus(orderId, status);
    }

    public PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId) {
        return paymentClient.getPaymentRecordOfClient(clientId).getBody();
    }

    public ResponseEntity<String> updateClientGrade(
        UserUpdateGradeRequestDto userUpdateGradeRequestDto) {
        return paymentClientClient.updateClientGrade(userUpdateGradeRequestDto);
    }

    public ResponseEntity<String> giveRewardCoupon(HttpHeaders headers, long amount) {
        CouponPaymentRewardRequestDto couponPaymentRewardRequestDto = CouponPaymentRewardRequestDto.builder().paymentValue(amount).build();
        return paymentCouponClient.getUserPaymentValue(headers, couponPaymentRewardRequestDto);
    }

    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(String tossOrderId) {
        return paymentClient.getPostProcessRequiredPaymentResponseDto(tossOrderId).getBody();
    }

}