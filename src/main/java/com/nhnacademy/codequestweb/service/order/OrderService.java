package com.nhnacademy.codequestweb.service.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.order.client.*;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderGetResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;

public interface OrderService {
    String viewClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto);
    String viewClientOrder(HttpServletRequest req, Model model, List<String> orderItemDto);
    String viewClientOrderDiscount(HttpServletRequest req, Model model);
    String viewClientOrderPayMethod(HttpServletRequest req, Model model);
    Long createClientOrder(HttpServletRequest req);
    Long createClientOrder(HttpServletRequest req, ClientOrderForm clientOrderForm);
    String viewNonClientOrder(HttpServletRequest req, Model model, List<String> orderItemDtoStringList);
    String viewNonClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto);
    Long createNonClientOrder(HttpServletRequest req, NonClientOrderForm nonClientOrderForm);
    ResponseEntity<String> getOrderStatus(Long orderDetailId);
    Page<ClientOrderGetResponseDto> getClientOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir);
    ClientOrderGetResponseDto getClientOrder(HttpHeaders headers, long orderId);
    void paymentCompleteClientOrder(HttpHeaders headers, long orderId);
    void cancelClientOrder(HttpHeaders headers, long orderId);
    void refundClientOrder(HttpHeaders headers, long orderId);
    void paymentCompleteNonClientOrder(HttpHeaders headers, long orderId);
    void cancelNonClientOrder(HttpHeaders headers, long orderId);
    void refundNonClientOrder(HttpHeaders headers, long orderId);
    NonClientOrderGetResponseDto findNonClientOrder(HttpHeaders headers, long orderId, String orderPassword);
    void updateOrderStatus(HttpHeaders headers, long orderId, String status);
    void getAllOrderList(HttpHeaders headers, Pageable pageable);
}
