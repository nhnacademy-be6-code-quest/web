package com.nhnacademy.codequestweb.controller.refund;

import com.nhnacademy.codequestweb.request.refund.PaymentCancelRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundAfterRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.request.refund.RefundRequestDto;
import com.nhnacademy.codequestweb.response.refund.RefundAdminResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundPolicyListResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundPolicyResponseDto;
import com.nhnacademy.codequestweb.response.refund.RefundSuccessResponseDto;
import com.nhnacademy.codequestweb.service.refund.RefundPolicyService;
import com.nhnacademy.codequestweb.service.refund.RefundService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@Slf4j
public class RefundController {

    private final RefundService refundService;
    private final RefundPolicyService refundPolicyService;

    @PostMapping("/client/payment/cancel")
    public String paymentCancel(@ModelAttribute PaymentCancelRequestDto paymentCancelRequestDto,
                                RedirectAttributes redirectAttributes) {

        refundService.cancelRequest(paymentCancelRequestDto);
        redirectAttributes.addFlashAttribute("alterMessage", "취소가 완료되었습니다.");
        return "redirect:/mypage/orders";
    }

    @GetMapping("/order/refund")
    public ResponseEntity<List<RefundPolicyResponseDto>> viewRefundInfo(
            @RequestParam long orderId) {
        try {
            List<RefundPolicyResponseDto> result = refundService.findRefundPay(orderId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/order/refund/request")
    public String requestRefund(@ModelAttribute RefundRequestDto requestDto, RedirectAttributes redirectAttributes) {
        RefundSuccessResponseDto dto = refundService.requestRefund(requestDto);
        redirectAttributes.addFlashAttribute("alterMessage", "환불금액이" + dto.getRefundAmount() + "원 적립될 예정입니다.");
        return "redirect:/mypage/orders";
    }

    @GetMapping("/client/refund/view")
    public ResponseEntity<RefundAdminResponseDto> refundUser(@RequestParam long orderId) {
        return ResponseEntity.ok(refundService.userRefund(orderId));
    }

    @PostMapping("/client/refund/sure")
    public String refundSure(@ModelAttribute RefundAfterRequestDto refundAfterRequestDto, RedirectAttributes redirectAttributes) {
        refundService.refundSure(refundAfterRequestDto);
        redirectAttributes.addFlashAttribute("alterMessage", "환불 완료");
        return "redirect:/admin/orders";
    }

    @GetMapping("/refund/policy/policies")
    public String refundPolicyList(HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        Page<RefundPolicyListResponseDto> policies = refundPolicyService.findPolices(page, size);
        List<String> refundPolicyTypes = List.of("반품", "단순변심","파본","파손");
        req.setAttribute("refundTypes",refundPolicyTypes);
        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "refundPolicy");
        req.setAttribute("activeSection", "refund");
        req.setAttribute("policies", policies);
        return "index";
    }
    @PostMapping("/refund/policy/register")
    public String refundPolicySave(HttpServletRequest req, @ModelAttribute
        RefundPolicyRegisterRequestDto requestDto){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        refundPolicyService.saveRefundPolicy(requestDto);

        return "redirect:/refund/policy/policies";

    }
}