package com.nhnacademy.codequestweb.config.payment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PaymentKey 를 받아 오는 Bean 을 등록하는 파일입니다. resources 하위 디렉터리에 key 디렉터리를 만들고 paymentKey.txt 를 넣어 주시면
 * 됩니다. 파일이 필요하신 경우에는 저에게 말씀해 주시면 감사하겠습니다. 추후 keyManager 에 의해 대체될 예정입니다.
 *
 * @author 김채호
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class PaymentKeyConfig {

    @Bean
    public String secretKey() throws IOException {
        File file = new File("src/main/resources/key/paymentKey.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str = br.readLine();
        System.out.println(str);
        return str;
    }
}