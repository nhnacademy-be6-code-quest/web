package com.nhnacademy.codequestweb.controller.product.everyone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.exception.product.CartProcessingException;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.SecretKeyUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.LinkedHashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {
    private static final String INDEX = "index";

    private static final String CART_REDIRECTION = "redirect:/cart/all";

    private static final String HOME_REDIRECTION = "redirect:/";

    private static final String ALTER_MESSAGE = "alterMessage";

    private static final TypeReference<List<CartRequestDto>> TYPE_REFERENCE = new TypeReference<List<CartRequestDto>>() {};

    private final CartService cartService;

    private final ObjectMapper objectMapper;

    private final List<CartRequestDto> emptyList = new ArrayList<>();

    @ExceptionHandler(CartProcessingException.class)
    public String handleCartJsonProcessingException(CartProcessingException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "장바구니 처리 과정에 일시적으로 문제가 발생했습니다.\n문제가 지속된다면 문의 부탁드립니다.");
        log.error(ex.getMessage());
        return HOME_REDIRECTION;
    }

    private List<CartRequestDto> getCartRequestDtoFromCookie(HttpServletRequest req){
        String encryptedCart = CookieUtils.getCookieValue(req, "cart");
        if (encryptedCart != null) {
            try {
                String cartJson = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
                return objectMapper.readValue(cartJson, TYPE_REFERENCE);
            } catch (Exception e) {
                log.warn("cart controller advice may have some problem with processing corrupted cookie. check the log with cart controller advice class");
                return emptyList;
            }
        }else{
            log.warn("cart controller advice may have some problem with processing deleted cookie. check the log with cart controller advice class");
            return emptyList;
        }
    }

    private Map<String, Boolean> makeJsonCartMap(List<CartGetResponseDto> cartList) {
        return cartList.stream()
                .collect(Collectors.toMap(
                        cart -> {
                            try {
                                return objectMapper.writeValueAsString(cart);
                            } catch (JsonProcessingException e) {
                                throw new CartProcessingException("error occurred while making json from dto, dto : {}" +  cart);
                            }
                        },
                        cart -> cart.productInventory() >= cart.productQuantityOfCart() && cart.productState() == 0,
                        (oldValue, newValue) -> oldValue, // 병합 함수 (충돌 해결)
                        LinkedHashMap::new // 맵 공급자
                ));
    }

    private String redirect(ResponseEntity<List<CartGetResponseDto>> responseEntity, Model model, String orderUrl) {
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        if (statusCode.is2xxSuccessful()){
            List<CartGetResponseDto> cartList = responseEntity.getBody();
            if (cartList == null || cartList.isEmpty()) {
                model.addAttribute("empty", true);
            }else {
                Map<String, Boolean> jsonCartMap = makeJsonCartMap(cartList);
                model.addAttribute("jsonCartMap", jsonCartMap);
                model.addAttribute("cartList", cartList);
            }
            model.addAttribute("view", "cart");
            model.addAttribute("orderUrl", orderUrl);
            return INDEX;
        }else if(statusCode.is3xxRedirection()){
            String redirectURL = HOME_REDIRECTION;
            URI location = responseEntity.getHeaders().getLocation();
            if (location != null){
                redirectURL = "redirect:" + location.toString();
            }
            return redirectURL;
        }else {
            return HOME_REDIRECTION;
        }
    }

    private String getGuestCart(HttpServletRequest req, Model model){
        List<CartRequestDto> cartListOfCookie = getCartRequestDtoFromCookie(req);

        if (cartListOfCookie == null || cartListOfCookie.isEmpty()) {
            model.addAttribute("empty", true);
            model.addAttribute("view", "cart");
            model.addAttribute("orderUrl", "/non-client/orders");
            return INDEX;
        }else{
            try {
                ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getGuestCartList(cartListOfCookie);
                return redirect(responseEntity, model, "/non-client/orders");
            }catch (FeignException e){
                throw new CartProcessingException("error occurred while get guest cart " + e.getMessage());
            }
        }
    }

    private String getClientCart(HttpServletRequest req, Model model){
        try {
            ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getClientCartList(CookieUtils.setHeader(req));
            return redirect(responseEntity, model, "/client/orders");
        }catch (FeignException e){
            throw new CartProcessingException("error occurred while get client cart " + e.getMessage());
        }
    }

    @GetMapping("/cart/all")
    public String cartAll(HttpServletRequest req, Model model){
        if (CookieUtils.isGuest(req)) {
            return getGuestCart(req, model);
        } else{
            return getClientCart(req, model);
        }
    }

    @PostMapping("/cart/add")
    public ResponseEntity<Void> addCart(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute @Valid CartRequestDto cartRequestDto){
        List<CartRequestDto> cartListOfCookie = getCartRequestDtoFromCookie(req);

        Long quantity = 0L;
        CartRequestDto cartDto = cartRequestDto;
        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

        for (CartRequestDto cartItem : cartListOfCookie) {
            if (cartItem.productId().equals(cartRequestDto.productId())) {
                if(CookieUtils.isGuest(req)){
                    cartDto = CartRequestDto.builder()
                            .productId(cartItem.productId())
                            .quantity(cartItem.quantity() + cartRequestDto.quantity())
                            .build();
                }
                cartRequestDtoToDelete.add(cartItem);
            }
        }

        try {
            ResponseEntity<SaveCartResponseDto> response;

            cartListOfCookie.removeAll(cartRequestDtoToDelete);

            if (CookieUtils.isGuest(req)) {
                response = cartService.addGuestCartItem(cartDto);
            } else{
                response = cartService.addClientCartItem(CookieUtils.setHeader(req), cartDto);
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                SaveCartResponseDto saveCartResponseDto = response.getBody();
                if (saveCartResponseDto != null) {
                    quantity = saveCartResponseDto.savedCartQuantity();
                }
            }
            cartListOfCookie.add(CartRequestDto.builder()
                    .productId(cartDto.productId())
                    .quantity(quantity).build());


            CookieUtils.deleteCookieValue(resp, "cart");
            CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);

            return ResponseEntity.status(200).build();
        }catch (Exception e){
            throw new CartProcessingException("error occurred while saving cart item" + e.getMessage());
        }
    }

    @PutMapping("/cart/update")
    public String updateCart(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute @Valid CartRequestDto cartRequestDto, RedirectAttributes redirectAttributes) {
        List<CartRequestDto> cartListOfCookie = getCartRequestDtoFromCookie(req);

        ResponseEntity<SaveCartResponseDto> response;
        Long quantity = 0L;
        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

        for (CartRequestDto cartItem : cartListOfCookie) {
            if (cartItem.productId().equals(cartRequestDto.productId())) {
                cartRequestDtoToDelete.add(cartItem);
            }
        }

        try {
            cartListOfCookie.removeAll(cartRequestDtoToDelete);
            if (CookieUtils.isGuest(req)) {
                response = cartService.updateGuestCartItem(cartRequestDto);
            }else {
                response = cartService.updateClientCartItem(CookieUtils.setHeader(req), cartRequestDto);
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                SaveCartResponseDto saveCartResponseDto = response.getBody();
                if (saveCartResponseDto != null) {
                    quantity = saveCartResponseDto.savedCartQuantity();
                }

                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "물건을 장바구니에 담았습니다.");
            }
            cartListOfCookie.add(CartRequestDto.builder()
                    .productId(cartRequestDto.productId())
                    .quantity(quantity).build());

            CookieUtils.deleteCookieValue(resp, "cart");
            CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);

            return CART_REDIRECTION;
        }catch (Exception e){
            throw new CartProcessingException("error occurred while updating cart item" + e.getMessage());
        }
    }

    @DeleteMapping("/cart/{productId}")
    public String deleteCart(HttpServletRequest req, HttpServletResponse resp, @PathVariable("productId") Long productId, RedirectAttributes redirectAttributes){
        List<CartRequestDto> cartListOfCookie = getCartRequestDtoFromCookie(req);

        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();
        for (CartRequestDto cartItem : cartListOfCookie) {
            if (cartItem.productId().equals(productId)) {
                cartRequestDtoToDelete.add(cartItem);
            }
        }

        try {
            if (CookieUtils.isGuest(req)) {
                cartListOfCookie.removeAll(cartRequestDtoToDelete);
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "장바구니에서 물건을 제거했습니다.");
            }else{
                ResponseEntity<Void> response = cartService.deleteClientCartItem(CookieUtils.setHeader(req), productId);
                if (response.getStatusCode().is2xxSuccessful()) {
                    cartListOfCookie.removeAll(cartRequestDtoToDelete);
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "장바구니에서 물건을 제거했습니다.");
                }
            }

            CookieUtils.deleteCookieValue(resp, "cart");
            CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
            return CART_REDIRECTION;
        }catch (Exception e){
            throw new CartProcessingException("error occurred while removing cart item" + e.getMessage());
        }
    }


    @DeleteMapping("/cart/clear")
    public String clearCart(HttpServletRequest req, HttpServletResponse resp, RedirectAttributes redirectAttributes){
        try {
            if (CookieUtils.isGuest(req)) {
                CookieUtils.deleteCookieValue(resp, "cart");
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "장바구니를 비웠습니다.");
            }else {
                ResponseEntity<Void> clearClientCart = cartService.clearClientAllCart(CookieUtils.setHeader(req));
                if (clearClientCart.getStatusCode().is2xxSuccessful()) {
                    CookieUtils.deleteCookieValue(resp, "cart");
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "장바구니를 비웠습니다.");
                }
            }
            return CART_REDIRECTION;
        }catch (Exception e){
            throw new CartProcessingException("error occurred while clearing cart" + e.getMessage());
        }
    }
}
