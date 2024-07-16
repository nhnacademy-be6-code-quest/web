package com.nhnacademy.codequestweb.service.refund;

import com.nhnacademy.codequestweb.client.refund.TossPayRefundClient;
import com.nhnacademy.codequestweb.request.refund.RefundTossRequestDto;
import com.nhnacademy.codequestweb.request.refund.TossRefundRequestDto;
import com.nhnacademy.codequestweb.response.refund.TossPaymentRefundResponseDto;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TossRefundService {

    private final String secretKey;
    private final TossPayRefundClient tossPayRefundClient;

    @PostConstruct
    public void init() {
        if (secretKey.isEmpty()) {
            log.error("secretKey is empty");
        }
    }

    public TossPaymentRefundResponseDto tossRefund(RefundTossRequestDto refundTossRequestDto) throws ParseException {
            // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode(secretKey.getBytes(StandardCharsets.UTF_8));
            String authorizations = "Basic " + new String(encodedBytes);
            String contentType = "application/json";

        TossRefundRequestDto dto = TossRefundRequestDto.builder()
            .cancelReason(refundTossRequestDto.getCancelReason()
            ).build();

        return tossPayRefundClient.cancelPayment(refundTossRequestDto.getTossPaymentKey(), dto, authorizations, contentType);

    }
}
