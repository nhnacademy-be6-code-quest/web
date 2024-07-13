package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.order.client.*;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // 비회원 단건 주문 - 바로 주문
    @PostMapping("/non-client/order")
    public String nonClientOrder(@ModelAttribute OrderItemDto orderItemDto, Model model, HttpServletRequest req){
        return orderService.viewNonClientOrder(req, model, orderItemDto);
    }

    // 비회원 복수 주문 - 장바구니 주문
    @PostMapping("/non-client/orders")
    public String nonClientOrder(@RequestParam("cartList") List<String> orderItemDtoStringList, Model model, HttpServletRequest req){
        return orderService.viewNonClientOrder(req, model, orderItemDtoStringList);
    }







    // 회원 단건 주문 - 바로 주문
    @PostMapping("/client/order")
    public String order(@ModelAttribute OrderItemDto orderItemDto, Model model, HttpServletRequest req){
        //return orderService.viewClientOrder(req, model, orderItemDto);
        return orderService.viewClientOrder2(req, model, orderItemDto);
    }

    // 회원 복수 주문 - 장바구니 주문
    @PostMapping("/client/orders")
    public String order(@RequestParam("cartList") List<String> orderItemDtoStringList, Model model, HttpServletRequest req){
        return orderService.viewClientOrder(req, model, orderItemDtoStringList);
        //return orderService.viewClientOrder2(req, model, orderItemDtoStringList);
    }

    @PostMapping("/client/order/process")
    public String processClientOrderForm(@ModelAttribute ClientOrderForm2 clientOrderForm2, HttpSession session){
        session.setAttribute("clientOrderForm2", clientOrderForm2);
        return "redirect:/client/order-discount";
    }

    @PostMapping("/client/order-discount")
    public String viewClientOrderDiscountForm(@ModelAttribute ClientOrderForm2 clientOrderForm2, Model model, HttpServletRequest req, HttpSession session){
        session.setAttribute("clientOrderForm2", clientOrderForm2);
        return orderService.viewClientOrderDiscount(req, model);
    }

    @PostMapping("/client/order-discount/process")
    public String processClientOrderDiscountForm(@ModelAttribute ClientOrderDiscountForm clientOrderDiscountForm, HttpSession session){
        session.setAttribute("clientOrderDiscountForm", clientOrderDiscountForm);
        return "redirect:/client/order-pay-method";
    }

    @PostMapping("/client/order-pay-method")
    public String viewClientOrderPayMethodForm(@ModelAttribute ClientOrderPayMethodForm clientOrderPayMethodForm, Model model, HttpServletRequest req, HttpSession session){
        session.setAttribute("clientOrderPayMethodForm", clientOrderPayMethodForm);
        return orderService.viewClientOrderPayMethod(req, model);
    }

    @PostMapping("/client/order-pay-method/process")
    public String processClientOrderPayMethodForm(@ModelAttribute ClientOrderPayMethodForm clientOrderPayMethodForm, HttpServletRequest request,
                                                  HttpSession session){
        ClientOrderForm2 clientOrderForm2 = (ClientOrderForm2) session.getAttribute("clientOrderForm2");
        ClientOrderDiscountForm clientOrderDiscountForm = (ClientOrderDiscountForm)session.getAttribute("clientOrderDiscountForm");
        return String.format("redirect:/client/order/%d/payment", orderService.createClientOrder2(request, clientOrderForm2, clientOrderDiscountForm, clientOrderPayMethodForm));
    }








    // 회원 주문 생성 feign 호출
    @PostMapping("/api/client/orders")
    public String tryClientOrder(HttpServletRequest request, @ModelAttribute ClientOrderForm clientOrderForm){
        return String.format("redirect:/client/order/%d/payment", orderService.createClientOrder(request, clientOrderForm));
    }

    // 비회원 주문 생성 feign 호출
    @PostMapping("/api/non-client/orders")
    public String tryNonClientOrder(HttpServletRequest request, @ModelAttribute NonClientOrderForm nonClientOrderForm){
        return String.format("redirect:/client/order/%d/payment", orderService.createNonClientOrder(request, nonClientOrderForm));
    }

    // 비회원 단건 주문 내역 조회 view
    @GetMapping("/non-client/order/find")
    public String orders(HttpServletRequest req){
        return "view/order/nonClientFindOrder";
    }

    @GetMapping("/non-client/order")
    @ResponseBody
    public NonClientOrderGetResponseDto nonClientFindOrder(HttpServletRequest req, @RequestParam("orderId") String orderIdStr, @RequestParam("orderPassword") String orderPassword){

        boolean tryToFindOrderSuccess = true;
        Long orderId = null;
        NonClientOrderGetResponseDto nonClientOrderGetResponseDto = null;
        try {
            Long.parseLong(orderIdStr);
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(req, "access"));
            orderId = Long.parseLong(orderIdStr);
            nonClientOrderGetResponseDto = orderService.findNonClientOrder(headers, orderId, orderPassword);
        } catch (NumberFormatException e) {
            tryToFindOrderSuccess = false;
        } catch(RuntimeException e){
            tryToFindOrderSuccess = false;
        }

        return nonClientOrderGetResponseDto;

    }

}
