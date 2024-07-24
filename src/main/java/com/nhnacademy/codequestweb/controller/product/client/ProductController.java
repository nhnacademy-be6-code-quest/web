package com.nhnacademy.codequestweb.controller.product.client;

import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductStateUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventorySetRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.ProductService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private static final String ALTER_MESSAGE = "alterMessage";

    private static final String REDIRECTION_PRODUCT_MAIN = "redirect:/product/books";

    private static final String ATTRIBUTE_VALUE = "요청 처리 과정에서 오류가 발생했습니다.";

    private String handleResponse(ResponseEntity<Void> response, RedirectAttributes redirectAttributes) {
        if (response == null){
            log.warn("the product response itself is null");
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECTION_PRODUCT_MAIN;
        }else {
            if (response.getStatusCode().is2xxSuccessful()){
                return "view/product/refresh";
            }else {
                log.warn("the product response is weird, status is not 2xx, but {}", response.getStatusCode());
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
                return REDIRECTION_PRODUCT_MAIN;
            }
        }
    }

    @PostMapping("/product/client/like")
    public String like(HttpServletRequest req,
                       RedirectAttributes redirectAttributes,
                       @ModelAttribute ProductLikeRequestDto productLikeRequestDto) {
        try{
            ResponseEntity<Void> response = productService.saveProductLike(CookieUtils.setHeader(req), productLikeRequestDto);
            return handleResponse(response, redirectAttributes);
        }catch (FeignException e){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECTION_PRODUCT_MAIN;
        }
    }

    @DeleteMapping("/product/client/unlike")
    public String unlike(HttpServletRequest req,
                         RedirectAttributes redirectAttributes,
                         @RequestParam("productId") long productId) {
        try {
            ResponseEntity<Void> response = productService.deleteProductLike(CookieUtils.setHeader(req), productId);
            return handleResponse(response, redirectAttributes);
        }catch (FeignException e){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECTION_PRODUCT_MAIN;
        }
    }

    @PutMapping("/admin/product/state")
    public ResponseEntity<Void> updateBookState(HttpServletRequest req, @ModelAttribute @Valid ProductStateUpdateRequestDto dto){
        try {
            ResponseEntity<ProductUpdateResponseDto> response = productService.updateProductState(CookieUtils.setHeader(req), dto);
            return ResponseEntity.status(response.getStatusCode()).build();
        }catch (FeignException e){
            return ResponseEntity.status(e.status()).build();
        }
    }

    // 아직 웹에 뷰로 기능 구현 안 되어 있음
    @PutMapping("/admin/inventory/set")
    public ResponseEntity<Void> setBookInventory(HttpServletRequest req, @ModelAttribute @Valid InventorySetRequestDto dto){
        try {
            return productService.setProductInventory(CookieUtils.setHeader(req), dto);
        }catch (FeignException e){
            return ResponseEntity.status(e.status()).build();
        }
    }
}
