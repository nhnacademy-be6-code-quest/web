package com.nhnacademy.codequestweb.service.order;

import com.nhnacademy.codequestweb.request.order.field.OrderItemDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;

public interface OrderService {
    String viewClientOrder(HttpServletRequest req, Model model, List<String> orderItemDtoStringList);
    String viewClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto);
    Long createClientOrder(HttpServletRequest req, ClientOrderForm clientOrderForm);
    String viewNonClientOrder(HttpServletRequest req, Model model, List<String> orderItemDtoStringList);
    String viewNonClientOrder(HttpServletRequest req, Model model, OrderItemDto orderItemDto);
    Long createNonClientOrder(HttpServletRequest req, NonClientOrderForm nonClientOrderForm);
    ResponseEntity<String> getOrderStatus(Long orderDetailId);
}
