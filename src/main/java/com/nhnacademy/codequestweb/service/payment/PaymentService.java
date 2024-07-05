package com.nhnacademy.codequestweb.service.payment;

import com.nhnacademy.codequestweb.client.payment.PaymentUserClient;
import com.nhnacademy.codequestweb.client.payment.PaymentClient;
import com.nhnacademy.codequestweb.client.payment.PaymentOrderClient;
import com.nhnacademy.codequestweb.client.payment.TossPaymentsClient;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderValidationRequestDto;
import com.nhnacademy.codequestweb.request.payment.TossPaymentsRequestDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
    private final PaymentUserClient paymentUserClient;
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

    public boolean isValidTossPayment(
        PaymentOrderValidationRequestDto paymentOrderValidationRequestDto,
        String tossOrderId, long amount) {
        return paymentOrderValidationRequestDto != null
            && (paymentOrderValidationRequestDto.getOrderTotalAmount()
            - paymentOrderValidationRequestDto.getDiscountAmountByPoint()
            - paymentOrderValidationRequestDto.getDiscountAmountByCoupon()) == amount
            && (paymentOrderValidationRequestDto.getTossOrderId()
            .equals(tossOrderId));
    }

    public JSONObject approvePayment(String tossOrderId, long amount,
        String paymentKey) throws ParseException {
        // 시크릿 키 설정 (주의: 실제 환경에서는 안전하게 관리해야 함)
        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        TossPaymentsRequestDto tossPaymentsRequestDto = TossPaymentsRequestDto.builder()
            .paymentKey(paymentKey)
            .orderId(tossOrderId)
            .amount(amount)
            .build();

        String contentType = "application/json";

        // 일단 String 으로 받아 온 뒤에
        String tossPaymentsApproveResponseString = tossPaymentsClient.approvePayment(
            tossPaymentsRequestDto, authorizations, contentType);

        // 다시 한 번 JSONObject 로 변환한다.
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(tossPaymentsApproveResponseString);
    }

    public TossPaymentsResponseDto parseJSONObject(JSONObject jsonObject) {
        TossPaymentsResponseDto tossPaymentsResponseDto = TossPaymentsResponseDto.builder()
            .orderName(jsonObject.get("orderName").toString())
            .totalAmount(Long.parseLong(jsonObject.get("totalAmount").toString()))
            .method(jsonObject.get("method").toString())
            .paymentKey(jsonObject.get("paymentKey").toString())
            .build();

        if (tossPaymentsResponseDto.getMethod().equals("카드")) {
            tossPaymentsResponseDto.setCardNumber(
                (String) ((JSONObject) jsonObject.get("card")).get("number"));
        } else if (tossPaymentsResponseDto.getMethod().equals("가상계좌")) {
            tossPaymentsResponseDto.setAccountNumber(
                (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
        } else if (tossPaymentsResponseDto.getMethod().equals("계좌이체")) {
            tossPaymentsResponseDto.setBank(
                (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
        } else if (tossPaymentsResponseDto.getMethod().equals("휴대폰")) {
            tossPaymentsResponseDto.setCustomerMobilePhone(
                (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
        }
        return tossPaymentsResponseDto;
    }

    public PaymentOrderRequestDto findPaymentOrderRequestDtoByOrderId(long orderId) {
        return paymentOrderClient.findPaymentOrderRequestDtoByOrderId(orderId);
    }

    public String findClientNameByHttpHeaders(HttpHeaders httpHeaders) {
        ResponseEntity<ClientPrivacyResponseDto> privacyResponseDtoResponseEntity = paymentUserClient.getPrivacy(
            httpHeaders);
        return privacyResponseDtoResponseEntity.getBody().getClientName();
    }
}