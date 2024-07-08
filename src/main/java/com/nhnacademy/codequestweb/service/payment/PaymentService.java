package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.payment.PaymentClient;
import com.nhnacademy.codequestweb.client.payment.PaymentCouponClient;
import com.nhnacademy.codequestweb.client.payment.PaymentOrderClient;
import com.nhnacademy.codequestweb.client.payment.PaymentPointClient;
import com.nhnacademy.codequestweb.client.payment.PaymentProductClient;
import com.nhnacademy.codequestweb.client.payment.TossPaymentsClient;
import com.nhnacademy.codequestweb.request.payment.PaymentAccumulatePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentCompletedCouponRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto2;
import com.nhnacademy.codequestweb.request.payment.PaymentProductRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.ProductOrderDetailOptionRequestDto;
import com.nhnacademy.codequestweb.request.payment.ProductOrderDetailRequestDto;
import com.nhnacademy.codequestweb.request.payment.TossPaymentsRequestDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import feign.Response;
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
    private final String secretKey;

    @PostConstruct
    public void init() {
        if (secretKey.isEmpty()) {
            log.error("secretKey is empty");
        }
    }

    public void savePayment(long orderId, TossPaymentsResponseDto tossPaymentsResponseDto) {
        paymentClient.savePayment(orderId, tossPaymentsResponseDto);
    }

    public boolean isValidTossPayment(PaymentOrderRequestDto2 paymentOrderRequestDto2,
        String tossOrderId, long amount) {
        return paymentOrderRequestDto2 != null
            && paymentOrderRequestDto2.getTossOrderId().equals(tossOrderId)
            && paymentOrderRequestDto2.getOrderTotalAmount()
            - paymentOrderRequestDto2.getDiscountAmountByCoupon()
            - paymentOrderRequestDto2.getDiscountAmountByPoint() == amount;
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

    public PaymentOrderRequestDto findPaymentOrderRequestDtoByOrderId(long orderId) {
        return paymentOrderClient.findPaymentOrderRequestDtoByOrderId(orderId);
    }

    public PaymentOrderRequestDto2 findPaymentOrderRequestDto2ByOrderId(long orderId) {
        return paymentOrderClient.findPaymentOrderRequestDto2ByOrderId(orderId);
    }

    public ResponseEntity<String> useCoupon(
        PaymentCompletedCouponRequestDto paymentCompletedCouponRequestDto) {
        return paymentCouponClient.paymentUsedCoupon(paymentCompletedCouponRequestDto);
    }

    public ResponseEntity<String> usePoint(PaymentUsePointRequestDto paymentUsePointRequestDto) {
        return paymentPointClient.usePoint(paymentUsePointRequestDto);
    }

    public ResponseEntity<String> accumulatePoint(PaymentAccumulatePointRequestDto paymentAccumulatePointRequestDto) {
        return paymentPointClient.accumulatePoint(paymentAccumulatePointRequestDto);
    }

    public ResponseEntity<String> reduceInventory(List<ProductOrderDetailRequestDto> productOrderDetailRequestDtoList) {
        List<PaymentProductRequestDto> productOrderRequestDtoList = new ArrayList<>();
        for (ProductOrderDetailRequestDto productOrderDetailRequestDto : productOrderDetailRequestDtoList) {
            productOrderRequestDtoList.add(PaymentProductRequestDto.builder()
                    .productId(productOrderDetailRequestDto.getProductId())
                    .quantity(productOrderDetailRequestDto.getQuantity())
                .build());
            for (ProductOrderDetailOptionRequestDto productOrderDetailOptionRequestDto : productOrderDetailRequestDto.getProductOrderDetailOptionRequestDtoList()) {
                productOrderRequestDtoList.add(PaymentProductRequestDto.builder()
                        .productId(productOrderDetailOptionRequestDto.getProductId())
                        .quantity(productOrderDetailOptionRequestDto.getOptionProductQuantity())
                    .build());
            }
        }
        return paymentProductClient.reduceInventory(productOrderDetailRequestDtoList);
    }

    public ResponseEntity<String> changeOrderStatusCompletePayment(Long orderId) {
        return paymentOrderClient.changeOrderStatusCompletePayment(orderId);
    }
}