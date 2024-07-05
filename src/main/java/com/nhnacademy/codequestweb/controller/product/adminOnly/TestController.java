package com.nhnacademy.codequestweb.controller.product.adminOnly;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test(@RequestParam("test") List<String> tests) throws Exception {
        log.info("list is : {}", tests);
        String test1 = tests.toString();

        String test2 = tests.get(0);

//        CartGetResponseDto json1 = (CartGetResponseDto) test1;

        log.info("test1 : {}", test1);

        log.info("test2 : {}", test2);

//        CartGetResponseDto json2 = (CartGetResponseDto) new ObjectMapper().readValue(test1, new TypeReference<>() {});
        ObjectMapper objectMapper = new ObjectMapper();

        List<CartGetResponseDto> json2 = objectMapper.readValue(test1, new TypeReference<List<CartGetResponseDto>>() {});
        List<OrderItemDto> orderItemDtoList = json2.stream()
                        .map(cartGetResponseDto -> OrderItemDto.builder()
                                .productId(cartGetResponseDto.productId())
                                .quantity(cartGetResponseDto.productQuantityOfCart())
                                .categoryId(cartGetResponseDto.categoryMapOfIdAndName().keySet().stream().toList())
                                .build())
                                .toList();
        log.info("dto is : {}", json2);

        log.info("order dto list : {}", orderItemDtoList);

        return "redirect:/";
    }
}
