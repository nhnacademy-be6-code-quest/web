package com.nhnacademy.codequestweb.controller.product.clientOnly;


import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductStateUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryDecreaseRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryIncreaseRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventorySetRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.ProductService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product/client/like")
    public String like(HttpServletRequest req, Model model, @ModelAttribute ProductLikeRequestDto productLikeRequestDto) {
        log.info(" header of like:{}", CookieUtils.setHeader(req));

        log.info("like: {}", productLikeRequestDto);
        ResponseEntity<Void> response = productService.saveProductLike(CookieUtils.setHeader(req), productLikeRequestDto);
        return "view/product/refresh";
    }

    @DeleteMapping("/product/client/unlike")
    public String unlike(HttpServletRequest req, Model model, @RequestParam("productId") long productId) {
        ResponseEntity<Void> response = productService.deleteProductLike(CookieUtils.setHeader(req), productId);
        return "view/product/refresh";
    }

    @PutMapping("/state")
    public String updateBookState(HttpServletRequest req, @ModelAttribute @Valid ProductStateUpdateRequestDto dto){
        ResponseEntity<ProductUpdateResponseDto> responseEntity = productService.updateProductState(CookieUtils.setHeader(req), dto);
        return "redirect:/";
    }

    @PutMapping("/inventory/increase")
    public String increaseBookInventory(HttpServletRequest req, @ModelAttribute @Valid
    List<InventoryIncreaseRequestDto> dtoList){
        ResponseEntity<Void> responseEntity = productService.increaseProductInventory(dtoList);
        return "redirect:/";
    }

    @PutMapping("/inventory/decrease")
    public String decreaseBookInventory(HttpServletRequest req, @ModelAttribute @Valid
    List<InventoryDecreaseRequestDto> dtoList){
        ResponseEntity<Void> responseEntity = productService.decreaseProductInventory(dtoList);
        return "redirect:/";
    }

    @PutMapping("/inventory/set")
    public String setBookInventory(HttpServletRequest req, @ModelAttribute @Valid InventorySetRequestDto dto){
        ResponseEntity<Void> responseEntity = productService.setProductInventory(CookieUtils.setHeader(req), dto);
        return "redirect:/";
    }

}