package com.nhnacademy.codequestweb.config.payment;

import com.nhnacademy.codequestweb.client.payment.NhnKeyManagerClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * NHN KeyManager 를 사용하여 Toss 결제에 필요한 비밀 키(secretKey)를 관리합니다. NHN Cloud 에 IP 를 등록하지 않은 외부 IP 에서는
 * secretKey 를 가져 올 수 없습니다.
 *
 * @author 김채호
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class PaymentKeyConfig {

    private final NhnKeyManagerClient nhnKeyManagerClient;

    /**
     * Toss 결제에 필요한 비밀 키(secretKey)를 반환합니다. NHN Cloud 외부 IP 에서 secretKey 에 접근할 수 없는 경우 빈 문자열을 반환합니다.
     * 예를 들면, CI가 진행 될 때 깃허브는 secretKey 를 가져올 수 없습니다.
     *
     * @return Toss 결제에 필요한 비밀 키(secretKey)
     */
    @Bean
    public String secretKey() { // 테스트 환경에서 돌아가지 않게...
        try {
            JSONObject jsonObject = nhnKeyManagerClient.getTossSecretKey();
            Map<String, Object> responseMap = (Map<String, Object>) jsonObject;
            Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
            String secretKey = (String) bodyMap.get("secret");
            return secretKey;
        } catch (NullPointerException e) {
            return "";
        }
    }
}
