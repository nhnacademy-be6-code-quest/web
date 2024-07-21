package com.nhnacademy.codequestweb.controller.product.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    @PostMapping
    public String test(@RequestParam("test") List<String> tests) throws Exception {
        log.info("list is : {}", tests);
        String test1 = tests.toString();

        String test2 = tests.get(0);

        log.info("test1 : {}", test1);

        log.info("test2 : {}", test2);

        ObjectMapper objectMapper = new ObjectMapper();

        List<CartGetResponseDto> json2 = objectMapper.readValue(test1, new TypeReference<List<CartGetResponseDto>>() {});
        List<OrderItemDto> orderItemDtoList = json2.stream()
                        .map(cartGetResponseDto -> OrderItemDto.builder()
                                .productId(cartGetResponseDto.productId())
                                .quantity(cartGetResponseDto.productQuantityOfCart())
                                .categoryIdList(cartGetResponseDto.categorySet().stream().map(ProductCategory::productCategoryId
                                ).toList())
                                .packable(cartGetResponseDto.packable())
                                .build())
                                .toList();
        log.info("dto is : {}", json2);

        log.info("order dto list : {}", orderItemDtoList);

        return "redirect:/";
    }

    @GetMapping("/2")
    public String test2(){
        return "/view/product/toast";
    }

    @PostMapping("/test")
    public String test22(@RequestParam("productDescription") String description, Model model){
        log.info("description is : {}", description);
        model.addAttribute("test", description);
        return "/view/product/toast2";
    }

    @GetMapping("/3")
    public String test3(){
        return "/view/product/test33";
    }

    @GetMapping("/4")
    public String test4(){
        return "packagingRegisterForm";
    }
}
