package com.nhnacademy.codequestweb.config.cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.SecretKeyUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
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

    private final List<CartRequestDto> emptyList = new ArrayList<>();

    private final CartService cartService;

    private void restoreClientCartList(HttpServletRequest req, HttpServletResponse resp, Model model) throws Exception {
        List<CartRequestDto> cartListOfCookie = cartService.restoreClientCartList(CookieUtils.setHeader(req)).getBody();
        log.warn("trying restore : {}", cartListOfCookie);
        model.addAttribute("cart", cartListOfCookie);
        CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
    }

    @ModelAttribute
    public void addAttributes(HttpServletRequest req, HttpServletResponse resp, Model model) throws Exception {
        String encryptedCart = CookieUtils.getCookieValue(req, "cart");

        if (encryptedCart != null) {
            try {
                String cartJson = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
                List<CartRequestDto> cartListOfCookie = objectMapper.readValue(cartJson, new TypeReference<List<CartRequestDto>>() {});
                model.addAttribute("cart", cartListOfCookie);
            } catch (IllegalArgumentException | IllegalBlockSizeException | BadPaddingException e) {
                log.warn("cookie has been corrupted");
                CookieUtils.deleteCookieValue(resp, "cart");
                if (!CookieUtils.isGuest(req)){
                   restoreClientCartList(req, resp, model);
                }else{
                    model.addAttribute("cart", emptyList);
                }
            } catch (Exception e) {
                model.addAttribute("cart", emptyList);
                log.error("error while decrypting cart", e);
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
