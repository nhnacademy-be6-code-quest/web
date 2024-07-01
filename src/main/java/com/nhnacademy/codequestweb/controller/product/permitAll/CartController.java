package com.nhnacademy.codequestweb.controller.product.permitAll;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.SecretKeyUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CartController {


    @GetMapping("/cart/all")
    public String cartAll(HttpServletRequest req, Model model) throws Exception {
        String accessToken = CookieUtils.getCookieValue(req, "access");
        String refreshToken = CookieUtils.getCookieValue(req, "refresh");

        if (accessToken != null && refreshToken != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            String encryptedCart = CookieUtils.getCookieValue(req, "cart");
            String cart = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
            List<CartGetResponseDto> cartList = objectMapper.readValue(cart, new TypeReference<List<CartGetResponseDto>>() {});
            model.addAttribute("cartList", cartList);
        }

        else{
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(req,"access"));
            headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
        }

        return "index";
    }
}
