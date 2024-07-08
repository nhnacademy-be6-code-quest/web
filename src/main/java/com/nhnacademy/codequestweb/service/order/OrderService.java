package com.nhnacademy.codequestweb.service.order;

import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface OrderService {
    String viewClientOrder(HttpServletRequest req, Model model);
    Long createClientOrder(HttpServletRequest req, ClientOrderForm clientOrderForm);
    String viewNonClientOrder(HttpServletRequest req, Model model);
    Long createNonClientOrder(HttpServletRequest req, NonClientOrderForm nonClientOrderForm);
    ResponseEntity<String> getOrderStatus(Long orderDetailId);
}
