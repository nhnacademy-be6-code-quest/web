package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderDiscountForm;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderPayMethodForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.service.order.AdminOrderService;
import com.nhnacademy.codequestweb.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final AdminOrderService adminOrderService;

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
        return orderService.viewClientOrder(req, model, orderItemDto);
    }

    // 회원 복수 주문 - 장바구니 주문
    @PostMapping("/client/orders")
    public String order(@RequestParam("cartList") List<String> orderItemDtoStringList, Model model, HttpServletRequest req){
        return orderService.viewClientOrder(req, model, orderItemDtoStringList);
    }

    // 회원 쿠폰 및 포인트 적용 페이지
    @PostMapping("/client/order-discount")
    public String viewClientOrderDiscountForm(@ModelAttribute ClientOrderForm clientOrderForm, Model model, HttpServletRequest req){
        req.getSession().setAttribute("clientOrderForm", clientOrderForm);
        return orderService.viewClientOrderDiscount(req, model);
    }

    // 회원 결제수단 선택 페이지
    @PostMapping("/client/order-pay-method")
    public String viewClientOrderPayMethodForm(@ModelAttribute ClientOrderDiscountForm clientOrderDiscountForm, Model model, HttpServletRequest req){
        req.getSession().setAttribute("clientOrderDiscountForm", clientOrderDiscountForm);
        return orderService.viewClientOrderPayMethod(req, model);
    }

    // 회원 주문 진행
    @PostMapping("/client/order/process")
    public String processClientOrderPayMethodForm(@ModelAttribute ClientOrderPayMethodForm clientOrderPayMethodForm, HttpServletRequest req){
        req.getSession().setAttribute("clientOrderPayMethodForm", clientOrderPayMethodForm);
        return String.format("redirect:/client/order/payment?tossOrderId=%s", orderService.saveClientTemporalOrder(req));
    }

    // 비회원 주문 진행
    @PostMapping("/non-client/order/process")
    public String tryNonClientOrder(HttpServletRequest request, @ModelAttribute NonClientOrderForm nonClientOrderForm){
        orderService.saveNonClientTemporalOrder(request, nonClientOrderForm);
        return String.format("redirect:/client/order/payment?tossOrderId=%s", nonClientOrderForm.getTossOrderId());
    }

    // 비회원 단건 주문 내역 조회 view
    @GetMapping("/non-client/order/find")
    public String orders(HttpServletRequest req, Model model){
        model.addAttribute("view", "nonClientFindOrder");
        return "index";
    }

    @PostMapping("/order/{orderId}/update")
    public String updateOrderStatus(HttpServletRequest request, @PathVariable long orderId, @RequestParam("status") String orderStatus, @RequestParam(value = "orderNo", defaultValue = "0") int orderNo){
        adminOrderService.updateOrderStatus(request, orderId, orderStatus);
        return String.format("redirect:/admin/orders?pageNo=%d&pageSize=20", orderNo);
    }

}