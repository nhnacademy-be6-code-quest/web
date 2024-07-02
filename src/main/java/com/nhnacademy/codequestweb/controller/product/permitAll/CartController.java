package com.nhnacademy.codequestweb.controller.product.permitAll;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.SecretKeyUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    private final String ACCESS = "access";
    private final String REFRESH = "refresh";

    private boolean isGuest(HttpServletRequest req) {
        String accessToken = CookieUtils.getCookieValue(req, ACCESS);
        String refreshToken = CookieUtils.getCookieValue(req, REFRESH);
        return accessToken == null && refreshToken == null;
    }

    @GetMapping("/cart/all")
    public String cartAll(HttpServletRequest req, Model model) throws Exception {
        if (isGuest(req)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String encryptedCart = CookieUtils.getCookieValue(req, "cart");

            if (encryptedCart == null) {
                model.addAttribute("empty", true);
            } else{
                String cartJson = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
                List<CartRequestDto> cartListOfCookie = objectMapper.readValue(cartJson, new TypeReference<List<CartRequestDto>>() {});
//                Map<Long, Long> cartListOfCookie = objectMapper.readValue(cartJson, new TypeReference<Map<Long, Long>>(){}>());

                if (cartListOfCookie == null || cartListOfCookie.isEmpty()) {
                    model.addAttribute("empty", true);
                }else{
                    ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getGuestCartList(cartListOfCookie);
                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
                        List<CartGetResponseDto> cartList = responseEntity.getBody();

                        if (cartList == null || cartList.isEmpty()) {
                            model.addAttribute("empty", true);
                        }else {
                            for (CartGetResponseDto cartGetResponseDto : cartList) {
                                log.info(cartGetResponseDto.toString());
                            }
                            model.addAttribute("cartList", cartList);
                        }
                        return "index";
                    }
                    return "error";
                }
            }

//            if (encryptedCart != null) {
//                String cartJson = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
//                List<CartRequestDto> cartListOfCookie = objectMapper.readValue(cartJson, new TypeReference<List<CartRequestDto>>() {});
//
//                if (cartListOfCookie != null && !cartListOfCookie.isEmpty()) {
//                    ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getGuestCartList(cartListOfCookie);
//
//                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                        List<CartGetResponseDto> cartList = responseEntity.getBody();
//
//                        if (cartList == null || cartList.isEmpty()) {
//                            model.addAttribute("empty", true);
//                        }else {
//                            model.addAttribute("cartList", cartList);
//                        }
//                    }
//                }
//                //코드 꼬라지봐 이게 실환가
//            }else {
//                model.addAttribute("empty", true);
//            }
        }

        else{
            HttpHeaders headers = new HttpHeaders();
            headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));
            headers.set(REFRESH, CookieUtils.getCookieValue(req, REFRESH));
            ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getClientCartList(headers);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<CartGetResponseDto> cartList = responseEntity.getBody();
                if (cartList == null || cartList.isEmpty()) {
                    model.addAttribute("empty", true);
                }else {
                    model.addAttribute("cartList", cartList);
                }
            }else {
                return "redirect:/";
            }
        }
        return "index";
    }


    @PostMapping("/cart/add")
    public String addCart(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute @Valid CartRequestDto cartRequestDto, Model model) throws Exception {
        log.info("Add cart request: {}", cartRequestDto);
        if (isGuest(req)) {
            log.info("Add cart guest");
            ObjectMapper objectMapper = new ObjectMapper();
            String encryptedCart = CookieUtils.getCookieValue(req, "cart");
            List<CartRequestDto> cartListOfCookie;
            if (encryptedCart == null) {
                cartListOfCookie = new ArrayList<>();
                cartListOfCookie.add(cartRequestDto);
                String cartJson = objectMapper.writeValueAsString(cartListOfCookie);
                String encryptedCartJson = SecretKeyUtils.encrypt(cartJson, SecretKeyUtils.getSecretKey());
                CookieUtils.setCookieValue(resp,"cart", encryptedCartJson);
            } else {
                CookieUtils.deleteCookieValue(resp, "cart");


                String cartJson = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
                cartListOfCookie = objectMapper.readValue(cartJson, new TypeReference<List<CartRequestDto>>() {});
                //재고보다 많으면 빠꾸시켜야되는데..
                //이거 프론트에서 막으려면 막을 순.. 있긴 한데 데이터 정합성이 맞을지 모르겠네.. 디비 접속하는게 더 정확할 거 같긴 한데.

                boolean isExist = false;
                for (CartRequestDto cartItem : cartListOfCookie) {
                    if (cartItem.productId().equals(cartRequestDto.productId())) {
                        CartRequestDto updatedCartRequestDto = CartRequestDto.builder()
                                .productId(cartItem.productId())
                                .quantity(cartItem.quantity() + cartRequestDto.quantity())
                                .build();
                        cartListOfCookie.remove(cartItem);
                        cartListOfCookie.add(updatedCartRequestDto);
                        isExist = true;
                    }
                }
                if (!isExist) {
                    cartListOfCookie.add(cartRequestDto);
                }

                log.info("cartListOfCookie: {}", cartListOfCookie);
                String updatedCartJson = objectMapper.writeValueAsString(cartListOfCookie);
                String encryptedUpdatedCartJson = SecretKeyUtils.encrypt(updatedCartJson, SecretKeyUtils.getSecretKey());
                CookieUtils.setCookieValue(resp,"cart", encryptedUpdatedCartJson);
            }
        }else {
            log.info("Add cart client");
            HttpHeaders headers = new HttpHeaders();
            headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));
            headers.set(REFRESH, CookieUtils.getCookieValue(req, REFRESH));
            ResponseEntity<SaveCartResponseDto> response = cartService.addCartItem(headers, cartRequestDto);
            if (response.getStatusCode().is2xxSuccessful()) {
                SaveCartResponseDto saveCartResponseDto = response.getBody();

            }
        }
        return "index";

    }


}
