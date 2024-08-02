package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.payment.*;
import com.nhnacademy.codequestweb.request.payment.*;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto.ProductOrderDetailOptionRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto;
import com.nhnacademy.codequestweb.request.point.PointRewardOrderRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryDecreaseRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentMethodResponseDto;
import com.nhnacademy.codequestweb.response.payment.PaymentsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void savePayment(HttpHeaders headers, PaymentsResponseDto paymentsResponseDto) {
        log.info("결제 및 주문 생성 시도");
        paymentClient.savePayment(headers, paymentsResponseDto);
    }

    public boolean isValidTossPayment(PaymentOrderApproveRequestDto paymentOrderApproveRequestDto,
        String orderCode, long amount) {
        return paymentOrderApproveRequestDto != null
            && paymentOrderApproveRequestDto.getOrderCode().equals(orderCode)
            && paymentOrderApproveRequestDto.getOrderTotalAmount()
            - paymentOrderApproveRequestDto.getDiscountAmountByCoupon()
            - paymentOrderApproveRequestDto.getDiscountAmountByPoint() == amount;
    }

    public PaymentsResponseDto approvePayment(HttpHeaders headers,String name, String orderCode, long amount,
        String paymentKey) {
        ApprovePaymentRequestDto approvePaymentRequestDto = new ApprovePaymentRequestDto();
        approvePaymentRequestDto.setPaymentKey(paymentKey);
        approvePaymentRequestDto.setAmount(amount);
        approvePaymentRequestDto.setOrderCode(orderCode);
        approvePaymentRequestDto.setMethodType(name);
        log.info("결제 승인 시도");
        return paymentClient.approvePayment(headers, approvePaymentRequestDto).getBody();
    }

    public PaymentOrderShowRequestDto findPaymentOrderShowRequestDtoByOrderId(HttpHeaders headers, String orderCode) {
        return orderClient.getPaymentOrderShowRequestDto(headers, orderCode).getBody();
    }

    public PaymentOrderApproveRequestDto findPaymentOrderApproveRequestDtoByOrderId(
        HttpHeaders headers, String orderCode) {
        return orderClient.getPaymentOrderApproveRequestDto(headers, orderCode).getBody();
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

    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(HttpHeaders headers, String orderCode) {
        return paymentClient.getPostProcessRequiredPaymentResponseDto(headers, orderCode).getBody();
    }

    public List<PaymentMethodResponseDto> getPaymentMethodList(HttpHeaders headers) {
        return paymentClient.getAllPaymentMethod(headers).getBody();
    }

}