package com.nhnacademy.codequestweb.controller.product.adminOnly;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.utils.SecretKeyUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/test")
public class TestController {
//    private static SecretKey secretKey;
//
//    public static SecretKey getSecretKey() throws NoSuchAlgorithmException {
//        if (secretKey == null) {
//            secretKey = generateSecretKey();
//        }
//        return secretKey;
//    }
//
//    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(256); // 256비트 길이의 AES 키 생성
//        return keyGenerator.generateKey();
//    }
//
//    public static String encrypt(String data, SecretKey secretKey) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
//        return Base64.getEncoder().encodeToString(encryptedBytes);
//    }
//
//    public static String decrypt(String encryptedData, SecretKey secretKey) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
//        return new String(decryptedBytes);
//    }

    @GetMapping
    public String test(HttpServletRequest req, HttpServletResponse resp) throws Exception {



//        Cookie[] cookies = req.getCookies();

        ObjectMapper mapper = new ObjectMapper();
        List<String> categories = new ArrayList<>();
        categories.add("test1");
        categories.add("test2");
        categories.add("test3");

//        Cookie cookie2 = new Cookie("username2", null);
//        cookie2.setMaxAge(0);
//        resp.addCookie(cookie2);

        String json = mapper.writeValueAsString(categories);
        String json2= SecretKeyUtils.encrypt(json, SecretKeyUtils.getSecretKey());
        Cookie cookie = new Cookie("username2", json2);
        resp.addCookie(cookie);
        System.out.println("before encrypt :" + json);
        System.out.println("after encrypt :" + json2);

        String value = cookie.getValue();

        String value2 = SecretKeyUtils.decrypt(value, SecretKeyUtils.getSecretKey());

        List<String> productCategories = mapper.readValue(value2, new TypeReference<List<String>>() {});

        System.out.println("before decrypt :" + value);

        String json3 = SecretKeyUtils.decrypt(json2, SecretKeyUtils.getSecretKey());
        System.out.println("after decrypt :" + json3);

        return "Test2(V0테스트용.안씀)";
    }
}
