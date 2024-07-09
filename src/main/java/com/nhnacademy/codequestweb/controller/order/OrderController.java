package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // 회원 단건 주문
    @PostMapping("/client/order")
    public String order(OrderItemDto orderItemDto, Model model, HttpServletRequest req){
        return orderService.viewClientOrder(req, model, orderItemDto);
    }

    // 회원 복수 주문
    @PostMapping("/client/orders")
    public String order(List<String> tests, Model model, HttpServletRequest req){
        return orderService.viewClientOrder(req, model, tests);
    }

    @GetMapping("/test/client/orders")
    public String order(Model model, HttpServletRequest req){
        return orderService.viewTestClientOrder(req, model);
    }

//    // TODO PostMapping 변경 후, 파라미터에 List<OrderItemDto> 추가
    // 회원 단건 주문
    @PostMapping("/non-client/order")
    public String nonClientOrder(@RequestBody List<OrderItemDto> orderItemDtoList, Model model, HttpServletRequest req){
        return orderService.viewNonClientOrder(req, model);
    }

    // 회원 복수 주묵
//    @PostMapping("/non-client/orders")
//    public String nonClientOrder(@RequestBody List<OrderItemDto> orderItemDtoList, Model model, HttpServletRequest req){
//        return orderService.viewNonClientOrder(req, model);
//    }



    @PostMapping("/api/client/orders")
    public String tryClientOrder(HttpServletRequest request, @ModelAttribute ClientOrderForm clientOrderForm){
        Long orderId = orderService.createClientOrder(request, clientOrderForm);
        return String.format("redirect:/client/order/%d/payment", orderId);
    }

    @PostMapping("/api/non-client/orders")
    public String tryNonClientOrder(HttpServletRequest request, @ModelAttribute NonClientOrderForm nonClientOrderForm){
        return String.format("redirect:/client/order/%d/payment", orderService.createNonClientOrder(request, nonClientOrderForm));
    }

}
