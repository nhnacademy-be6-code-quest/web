package com.nhnacademy.codequestweb.config.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CartControllerAdvice {
    private final ObjectMapper objectMapper;

    private final CartService cartService;

    private final List<CartRequestDto> emptyList = new ArrayList<>();


    private void restoreClientCartList(HttpServletRequest req, HttpServletResponse resp, Model model) throws Exception {
        List<CartRequestDto> cartListOfCookie = cartService.restoreClientCartList(CookieUtils.setHeader(req)).getBody();
        log.warn("trying restore : {}", cartListOfCookie);
        model.addAttribute("cart", cartListOfCookie);
        CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
    }

    @ModelAttribute
    public void addAttributes(HttpServletRequest req, HttpServletResponse resp, Model model) throws Exception {
        String encodedCart = CookieUtils.getCookieValue(req, "cart");
        if (encodedCart != null) {
            try {
                String cartJson = new String(Base64.getDecoder().decode(encodedCart));
                List<CartRequestDto> cartListOfCookie = objectMapper.readValue(cartJson, new TypeReference<List<CartRequestDto>>() {});
                model.addAttribute("cart", cartListOfCookie);
            } catch (IllegalArgumentException | JsonProcessingException e) {
                log.warn("cookie has been corrupted");
                if (!CookieUtils.isGuest(req)){
                   restoreClientCartList(req, resp, model);
                }else{
                    CookieUtils.setCartCookieValue(emptyList, objectMapper, resp);
                    model.addAttribute("cart", emptyList);
                }
            } catch (Exception e) {
                CookieUtils.setCartCookieValue(emptyList, objectMapper, resp);
                model.addAttribute("cart", emptyList);
                log.error("error while decrypting cart, message : {}", e.getMessage());
            }
        } else {
            log.warn("cookie has been deleted");
            try {
                if (!CookieUtils.isGuest(req)){
                    restoreClientCartList(req, resp, model);
                }else {
                    CookieUtils.setCartCookieValue(emptyList, objectMapper, resp);
                    model.addAttribute("cart", emptyList);
                }
            } catch (Exception e) {
                CookieUtils.setCartCookieValue(emptyList, objectMapper, resp);
                model.addAttribute("cart", emptyList);
                log.error("error while making new cart cookie");
            }
        }
    }
}
