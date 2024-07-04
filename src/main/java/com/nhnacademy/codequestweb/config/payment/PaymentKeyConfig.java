package com.nhnacademy.codequestweb.config.payment;

import com.nhnacademy.codequestweb.client.payment.NhnKeyManagerClient;
import jakarta.validation.constraints.Null;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * - KeyManager 로 관리 중입니다.
 * - IP가 NHN Cloud 에 없으면 secretKey 에 접근할 수 없습니다.
 *
 * @author 김채호
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class PaymentKeyConfig {

    private final NhnKeyManagerClient nhnKeyManagerClient;

    @Bean
    public String secretKey() { // 테스트 환경에서 돌아가지 않게...
        try{
            JSONObject jsonObject = nhnKeyManagerClient.getTossSecretKey();
            Map<String, Object> responseMap = (Map<String, Object>) jsonObject;
            Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
            String secretKey = (String) bodyMap.get("secret");
            return secretKey;
        } catch (NullPointerException e){
            return "";
        }
    }
}
