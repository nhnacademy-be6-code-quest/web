package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.coupon.CouponMyPageCouponResponseDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.common.OrderResponseDto;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {
// 결제페이지 연결 중
    private final OrderService orderService;

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

    // 비회원 단건 주문 - 바로 주문
    @PostMapping("/non-client/order")
    public String nonClientOrder(@ModelAttribute OrderItemDto orderItemDto, Model model, HttpServletRequest req){
        return orderService.viewNonClientOrder(req, model, orderItemDto);
    }

    // 비회원 복수 주문
    @PostMapping("/non-client/orders")
    public String nonClientOrder(@RequestParam("cartList") List<String> orderItemDtoStringList, Model model, HttpServletRequest req){
        return orderService.viewNonClientOrder(req, model, orderItemDtoStringList);
    }

    // 회원 주문 생성
    @PostMapping("/api/client/orders")
    public String tryClientOrder(HttpServletRequest request, @ModelAttribute ClientOrderForm clientOrderForm){
        return String.format("redirect:/client/order/%d/payment", orderService.createClientOrder(request, clientOrderForm));
    }

    // 비회원 주문 생성
    @PostMapping("/api/non-client/orders")
    public String tryNonClientOrder(HttpServletRequest request, @ModelAttribute NonClientOrderForm nonClientOrderForm){
        return String.format("redirect:/client/order/%d/payment", orderService.createNonClientOrder(request, nonClientOrderForm));
    }

    @GetMapping("/mypage/orders")
    public String maypageOrders(HttpServletRequest req,
                                @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo){

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "orders");

        Page<OrderResponseDto> orderResponseDtoList = orderService.getClientOrders(headers, pageSize, pageNo, "orderDatetime", "desc");

        req.setAttribute("orders", orderResponseDtoList.getContent());
        req.setAttribute("totalPages", orderResponseDtoList.getTotalPages());
        req.setAttribute("currentPage", orderResponseDtoList.getNumber());
        req.setAttribute("pageSize", orderResponseDtoList.getSize());

        return "index";
    }

    // 회원 단건 주문 조회 view
    @GetMapping("/non-client/order/view")
    public String orders(HttpServletRequest req){
        return "view/order/nonClientFindOrder";
    }

    @GetMapping("/non-client/order")
    @ResponseBody
    public OrderResponseDto nonClientFindOrder(HttpServletRequest req, @RequestParam("orderId") String orderIdStr, @RequestParam("orderPassword") String orderPassword){

        boolean tryToFindOrderSuccess = true;
        Long orderId = null;
        OrderResponseDto orderResponseDto = null;
        try {
            Long.parseLong(orderIdStr);
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(req, "access"));
            orderId = Long.parseLong(orderIdStr);
            orderResponseDto = orderService.findNonClientOrder(headers, orderId, orderPassword);
        } catch (NumberFormatException e) {
            tryToFindOrderSuccess = false;
        } catch(RuntimeException e){
            tryToFindOrderSuccess = false;
        }

        return orderResponseDto;

    }

}
