package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.order.ClientOrderPostRequestDto;
import com.nhnacademy.codequestweb.request.order.field.OrderItem;
import com.nhnacademy.codequestweb.response.order.ClientOrderPostResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    // test
    public String order(Model model, HttpServletRequest request) {
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(new OrderItem(1,2));
        orderItemList.add(new OrderItem(2,3));
        ClientOrderPostRequestDto orderRequestDto = new ClientOrderPostRequestDto(orderItemList);
        ResponseEntity<ClientOrderPostResponseDto> response = orderService.gotoOrder(orderRequestDto);
        model.addAttribute("orderResponseDto", response.getBody());
        return "view/order/order";
    }

    @PostMapping
    public String orderView(ClientOrderPostRequestDto orderRequestDto, Model model){
        ResponseEntity<ClientOrderPostResponseDto> response = orderService.gotoOrder(orderRequestDto);
        model.addAttribute("orderResponseDto", response.getBody());
        return "view/order/order";
    }

}
