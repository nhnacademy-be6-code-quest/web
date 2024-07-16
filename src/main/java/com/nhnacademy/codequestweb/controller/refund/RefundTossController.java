package com.nhnacademy.codequestweb.controller.refund;

import com.nhnacademy.codequestweb.client.refund.TossPayRefundClient;
import com.nhnacademy.codequestweb.request.refund.RefundTossRequestDto;
import com.nhnacademy.codequestweb.service.refund.TossRefundService;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RefundTossController {
    private final TossRefundService tossRefundService;

    @PostMapping("/client/order/{orderId}/refund")
    public String tossRefund(@PathVariable long orderId, @ModelAttribute RefundTossRequestDto refundTossRequestDto)
        throws ParseException {
        tossRefundService.tossRefund(refundTossRequestDto);
        System.out.println(orderId);
        return "redirect:/mypage/orders";
    }
}
