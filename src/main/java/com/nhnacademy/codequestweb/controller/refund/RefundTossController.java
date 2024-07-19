package com.nhnacademy.codequestweb.controller.refund;

import com.nhnacademy.codequestweb.request.refund.RefundTossRequestDto;
import com.nhnacademy.codequestweb.service.refund.RefundService;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class RefundTossController {
    private final RefundService refundService;
    @PostMapping("/client/order/{orderId}/refund")
    public String tossRefund(@PathVariable long orderId, @RequestBody RefundTossRequestDto refundTossRequestDto)
        throws ParseException {
        refundService.requestRefund(orderId);
        //tossRefundService.tossRefund(refundTossRequestDto);
        //refundService.saveRefund(orderId, refundTossRequestDto);
        return "redirect:/mypage/orders";
    }
}
