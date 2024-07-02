package com.nhnacademy.codequestweb.config.payment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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