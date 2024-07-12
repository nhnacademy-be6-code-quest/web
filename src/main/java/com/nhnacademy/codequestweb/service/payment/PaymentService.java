package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.order.OrderClient;
import com.nhnacademy.codequestweb.client.payment.PaymentClient;
import com.nhnacademy.codequestweb.client.payment.PaymentClientClient;
import com.nhnacademy.codequestweb.client.payment.PaymentCouponClient;
import com.nhnacademy.codequestweb.client.payment.PaymentOrderClient;
import com.nhnacademy.codequestweb.client.payment.PaymentPointClient;
import com.nhnacademy.codequestweb.client.payment.PaymentProductClient;
import com.nhnacademy.codequestweb.client.payment.TossPaymentsClient;
import com.nhnacademy.codequestweb.request.payment.ClientUpdateGradeRequestDto;
import com.nhnacademy.codequestweb.request.payment.CouponPaymentRewardRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentAccumulatePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentCompletedCouponRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto.ProductOrderDetailOptionRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.TossPaymentsRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryDecreaseRequestDto;
import com.nhnacademy.codequestweb.response.payment.PaymentGradeResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService /*implements PaymentService*/ {

    private final PaymentClient paymentClient;
    private final PaymentOrderClient paymentOrderClient;
    private final TossPaymentsClient tossPaymentsClient;
    private final PaymentCouponClient paymentCouponClient;
    private final PaymentPointClient paymentPointClient;
    private final PaymentProductClient paymentProductClient;
    private final OrderClient orderClient;
    private final PaymentClientClient paymentClientClient;
    private final String secretKey;

    @PostConstruct
    public void init() {
        if (secretKey.isEmpty()) {
            log.error("secretKey is empty");
        }
    }

    public void savePayment(HttpHeaders headers, long orderId,
        TossPaymentsResponseDto tossPaymentsResponseDto) {
        paymentClient.savePayment(headers, orderId, tossPaymentsResponseDto);
    }

    public boolean isValidTossPayment(PaymentOrderApproveRequestDto paymentOrderApproveRequestDto,
        String tossOrderId, long amount) {
        return paymentOrderApproveRequestDto != null
            && paymentOrderApproveRequestDto.getTossOrderId().equals(tossOrderId)
            && paymentOrderApproveRequestDto.getOrderTotalAmount()
            - paymentOrderApproveRequestDto.getDiscountAmountByCoupon()
            - paymentOrderApproveRequestDto.getDiscountAmountByPoint() == amount;
    }

    public TossPaymentsResponseDto approvePayment(String tossOrderId, long amount,
        String paymentKey) throws ParseException {
        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);
        String contentType = "application/json";

        TossPaymentsRequestDto tossPaymentsRequestDto = TossPaymentsRequestDto.builder()
            .paymentKey(paymentKey)
            .orderId(tossOrderId)
            .amount(amount)
            .build();

        // 승인 요청을 보내면서 + 응답을 받아 옴.
        String tossPaymentsApproveResponseString = tossPaymentsClient.approvePayment(
            tossPaymentsRequestDto, authorizations, contentType);

        // 다시 한 번 JSONObject 로 변환한다.
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(
            tossPaymentsApproveResponseString);

        String orderName = jsonObject.get("orderName").toString();
        String totalAmount = jsonObject.get("totalAmount").toString();
        String method = jsonObject.get("method").toString();
        String cardNumber = null;
        String accountNumber = null;
        String bank = null;
        String customerMobilePhone = null;

        if (method.equals("카드")) {
            cardNumber = ((JSONObject) jsonObject.get("card")).get("number").toString();
        } else if (method.equals("가상계좌")) {
            accountNumber = ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber")
                .toString();
        } else if (method.equals("계좌이체")) {
            bank = ((JSONObject) jsonObject.get("transfer")).get("bank").toString();
        } else if (method.equals("휴대폰")) {
            customerMobilePhone = ((JSONObject) jsonObject.get("mobilePhone")).get(
                "customerMobilePhone").toString();
        } else if (method.equals("간편결제")) {
            method =
                method + "-" + ((JSONObject) jsonObject.get("easyPay")).get("provider").toString();
        }

        return TossPaymentsResponseDto.builder()
            .orderName(orderName)
            .totalAmount(Long.parseLong(totalAmount))
            .method(method)
            .paymentKey(paymentKey)
            .cardNumber(cardNumber)
            .accountNumber(accountNumber)
            .bank(bank)
            .customerMobilePhone(customerMobilePhone)
            .build();
    }

    public PaymentOrderShowRequestDto findPaymentOrderShowRequestDtoByOrderId(HttpHeaders headers,
        long orderId) {
        return orderClient.getPaymentOrderShowRequestDto(headers, orderId).getBody();
    }

    public PaymentOrderApproveRequestDto findPaymentOrderApproveRequestDtoByOrderId(
        HttpHeaders headers, long orderId) {
        return orderClient.getPaymentOrderApproveRequestDto(headers, orderId).getBody();
    }

    public ResponseEntity<String> useCoupon(
        HttpHeaders headers, PaymentCompletedCouponRequestDto paymentCompletedCouponRequestDto) {
        return paymentCouponClient.paymentUsedCoupon(headers, paymentCompletedCouponRequestDto);
    }

    public ResponseEntity<String> usePaymentPoint(
        PaymentUsePointRequestDto paymentUsePointRequestDto, HttpHeaders httpHeaders) {
        return paymentPointClient.usePaymentPoint(paymentUsePointRequestDto, httpHeaders);
    }

    public ResponseEntity<String> accumulatePoint(HttpHeaders httpHeaders,
        PaymentAccumulatePointRequestDto paymentAccumulatePointRequestDto) {
        return paymentPointClient.rewardOrderPoint(httpHeaders, paymentAccumulatePointRequestDto);
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
        return paymentClient.getPaymentRecordOfClient(clientId);
    }

    public ResponseEntity<String> updateClientGrade(
        ClientUpdateGradeRequestDto clientUpdateGradeRequestDto) {
        return paymentClientClient.updateClientGrade(clientUpdateGradeRequestDto);
    }

    public ResponseEntity<String> giveRewardCoupon(
        CouponPaymentRewardRequestDto couponPaymentRewardRequestDto) {
        return paymentCouponClient.getUserPaymentValue(couponPaymentRewardRequestDto);
    }
}