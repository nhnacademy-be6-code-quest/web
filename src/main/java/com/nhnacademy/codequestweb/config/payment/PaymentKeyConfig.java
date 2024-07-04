package com.nhnacademy.codequestweb.config.payment;

import com.nhnacademy.codequestweb.client.payment.NhnKeyManagerClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * PaymentKey 를 받아 오는 Bean 을 등록하는 파일입니다. resources 하위 디렉터리에 key 디렉터리를 만들고 paymentKey.txt 를 넣어 주시면
 * 됩니다. 파일이 필요하신 경우에는 저에게 말씀해 주시면 감사하겠습니다. 추후 keyManager 에 의해 대체될 예정입니다.
 *
 * @author 김채호
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
//@Profile({"dev", "prod"})
@Profile("prod")
public class PaymentKeyConfig {

    private final NhnKeyManagerClient nhnKeyManagerClient;

    @Bean
    public String secretKey() { // 테스트 환경에서 돌아가지 않게...
        JSONObject jsonObject = nhnKeyManagerClient.getTossSecretKey();
        Map<String, Object> responseMap = (Map<String, Object>) jsonObject;
        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
        return (String) bodyMap.get("secret");
    }
}
