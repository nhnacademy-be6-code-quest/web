package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.order.OrderRequestDto;
import com.nhnacademy.codequestweb.response.order.OrderResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String order() {
        return "order/order";
    }

    @PostMapping
    public String orderView(OrderRequestDto orderRequestDto, Model model){
        ResponseEntity<OrderResponseDto> response = orderService.gotoOrder(orderRequestDto);
        model.addAttribute("orderResponseDto", response.getBody());
        return "order/order";
    }

}
