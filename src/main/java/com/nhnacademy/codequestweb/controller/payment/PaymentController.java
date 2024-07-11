package com.nhnacademy.codequestweb.controller.payment;

import com.nhnacademy.codequestweb.request.payment.PaymentAccumulatePointRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentCompletedCouponRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentUsePointRequestDto;
import com.nhnacademy.codequestweb.response.payment.TossPaymentsResponseDto;
import com.nhnacademy.codequestweb.service.payment.PaymentService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    // PaymentController 의 Service 는 PaymentService 만 있어야 함. 단일 책임의 원칙...
    // 하나의 컨트롤러가 여러 개의 서비스를 호출하면 1) 테스트 어렵고 2) 확장성도 떨어짐
    private final PaymentService paymentService;

    @GetMapping("/client/order/{orderId}/payment")
    public String savePayment(@RequestHeader HttpHeaders headers, @PathVariable long orderId,
        Model model) {
//        1. 주문에서 받은 값을 토대로 사용자에게 보여 주기
        PaymentOrderShowRequestDto paymentOrderShowRequestDto = paymentService.findPaymentOrderShowRequestDtoByOrderId(
            headers, orderId);
        model.addAttribute("paymentOrderShowRequestDto", paymentOrderShowRequestDto);
        model.addAttribute("successUrl",
            "https://book-store.shop/client/order/" + orderId + "/payment/success");
        model.addAttribute("failUrl",
            "https://book-store.shop/client/order/" + orderId + "/payment/fail");
        return "view/payment/tossPage";
    }

    @GetMapping("/client/order/{orderId}/payment/success")
    public String paymentResult(HttpServletRequest request,
        @PathVariable long orderId, Model model,
        @RequestParam(value = "orderId") String tossOrderId,
        @RequestParam long amount, @RequestParam String paymentKey) throws ParseException {

        HttpHeaders headers = CookieUtils.setHeader(request);

//        2. 결제 검증 및 승인 창에서 필요한 요소를 Order 에서 받아 오기 : @RequestHeader 로 해결함. // 303 ->
        PaymentOrderApproveRequestDto paymentOrderApproveRequestDto = paymentService.findPaymentOrderApproveRequestDtoByOrderId(
            headers, orderId);

//        3. 조작 확인하기 : 주문 정보가 일치하지 않으면 실패 페이지로 이동하기.
        if (!paymentService.isValidTossPayment(paymentOrderApproveRequestDto, tossOrderId,
            amount)) {
            model.addAttribute("isSuccess", false);
            model.addAttribute("code", "INVALID_ORDER");
            model.addAttribute("message", "주문 정보가 일치하지 않습니다.");
            log.warn("주문 아이디 : {} 에서 결제 조작이 의심됩니다.", orderId);
            return "view/payment/failed";
        }

        // 4. 재고 없으면 재고 없다고 하고, 결제 fail URL 로 이동하게 만들기
        boolean isProductInventoryReducedNormally = true;

        try {
            paymentService.decreaseProductInventory(
                paymentOrderApproveRequestDto.getProductOrderDetailList());
        } catch (FeignException.BadRequest e) {
            if (e.status() != 200) {
                log.error("상품의 재고 감소 처리에 실패했습니다. 상품의 재고가 0일 수 있습니다.\norderId: {}", orderId);
                model.addAttribute("isSuccess", false);
                model.addAttribute("code", "NO_PRODUCT_INVENTORY");
                model.addAttribute("message", "상품의 재고가 부족하거나 상품 관련 서비스 에러입니다.");
                return "view/payment/failed";
            }
        } catch (Exception e) {
            log.error("재고 감소 처리 중 알 수 없는 에러가 발생했습니다.\nproductOrderDetailList: {}\nerrorMessage: {}",
                paymentOrderApproveRequestDto.getProductOrderDetailList(),
                e.getMessage());
            isProductInventoryReducedNormally = false;
        }

        // 5. 쿠폰 사용, 포인트 사용, 포인트 적립, 주문 상태 바꾸기

        // 5-1) 쿠폰 사용하기
        boolean isCouponProcessedNormally = true;

        try {
            if (paymentOrderApproveRequestDto.getCouponId() != null) {
                ResponseEntity<String> couponRes = paymentService.useCoupon(headers,
                    PaymentCompletedCouponRequestDto.builder()
                        .couponId(paymentOrderApproveRequestDto.getCouponId())
                        .build());

                HttpStatusCode code = couponRes.getStatusCode();
                boolean couponUseStatusCodeIs200 = code.equals(HttpStatusCode.valueOf(200));

                if (!couponUseStatusCodeIs200) {
                    log.error("쿠폰 사용 처리에 실패했습니다.\n쿠폰 아이디: {}",
                        paymentOrderApproveRequestDto.getCouponId());
                    isCouponProcessedNormally = false;
                }
            }
        } catch (Exception e) {
            log.error("쿠폰 사용 처리 중 알 수 없는 에러가 발생했습니다.\ncouponId: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getCouponId(),
                paymentOrderApproveRequestDto.getClientId());
            isCouponProcessedNormally = false;
        }

        // 5-2) 포인트 사용하기
        boolean isPointUsedNormally = true;

        try {
            if (paymentOrderApproveRequestDto.getDiscountAmountByPoint() != 0) {
                PaymentUsePointRequestDto paymentUsePointRequestDto = new PaymentUsePointRequestDto();
                paymentUsePointRequestDto.setPointUsagePayment(
                    (int) paymentOrderApproveRequestDto.getDiscountAmountByPoint());

                ResponseEntity<String> pointUseResponseEntity = paymentService.usePaymentPoint(
                    paymentUsePointRequestDto, headers);

                HttpStatusCode pointUseStatusCode = pointUseResponseEntity.getStatusCode();
                boolean pointUseStatusCodeIs200 = pointUseStatusCode.equals(
                    HttpStatusCode.valueOf(200));

                if (!pointUseStatusCodeIs200) {
                    log.error("포인트 사용 처리에 실패했습니다.\nclientId: {}\ndiscountAmountByPoint: {}",
                        paymentOrderApproveRequestDto.getClientId(),
                        paymentOrderApproveRequestDto.getDiscountAmountByPoint());
                    isPointUsedNormally = false;
                }
            }
        } catch (Exception e) {
            log.error("포인트 사용 처리 중 알 수 없는 에러가 발생했습니다.\ndiscountAmountByPoint: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getDiscountAmountByPoint(),
                paymentOrderApproveRequestDto.getClientId());
            isPointUsedNormally = false;
        }

        // 5-3) 포인트 적립하기
        boolean isPointAccumulatedNormally = true;

        try {
            PaymentAccumulatePointRequestDto paymentAccumulatePointRequestDto = new PaymentAccumulatePointRequestDto();
            paymentAccumulatePointRequestDto.setAccumulatedPoint(
                (int) paymentOrderApproveRequestDto.getAccumulatedPoint());

            ResponseEntity<String> pointAccumulateResponseEntity = paymentService.accumulatePoint(
                headers, paymentAccumulatePointRequestDto);

            HttpStatusCode pointAccumulateStatusCode = pointAccumulateResponseEntity.getStatusCode();
            boolean pointAccumulateStatusCodeIs200 = pointAccumulateStatusCode.equals(
                HttpStatusCode.valueOf(200));

            if (!pointAccumulateStatusCodeIs200) {
                log.error("포인트 적립 처리에 실패했습니다.\nclientId: {}\naccumulatedPoint: {}",
                    paymentOrderApproveRequestDto.getClientId(),
                    paymentOrderApproveRequestDto.getAccumulatedPoint());
                isPointAccumulatedNormally = false;
            }
        } catch (Exception e) {
            log.error("포인트 적립 처리 중 알 수 없는 에러가 발생했습니다.\naccumulatedPoint: {}\nclientId: {}",
                paymentOrderApproveRequestDto.getAccumulatedPoint(),
                paymentOrderApproveRequestDto.getClientId());
            isPointAccumulatedNormally = false;
        }

        // 5-4) 주문 상태 바꾸기
        boolean isOrderStatusChangedToCompletedPayment = true;

        try {
            paymentService.changeOrderStatusCompletePayment(orderId, "결제완료");
        } catch (FeignException e) {
            if (e.status() != 200) {
                log.error("주문 상태를 결제 완료로 바꾸는 데에 실패했습니다.\norderId: {}", orderId);
                isOrderStatusChangedToCompletedPayment = false;
            }
        } catch (Exception e) {
            log.error("주문 상태를 결제 대기에서 결제 완료로 바꾸는 중 알 수 없는 에러가 발생했습니다.\norderId: {}", orderId);
            isOrderStatusChangedToCompletedPayment = false;
        }

        // 6. 결제 승인하기
        TossPaymentsResponseDto tossPaymentsResponseDto = paymentService.approvePayment(tossOrderId,
            amount, paymentKey);

        // 7. View 보여주기
        paymentService.savePayment(headers, orderId, tossPaymentsResponseDto);
        model.addAttribute("isSuccess", true);
        model.addAttribute("isCouponProcessedNormally", isCouponProcessedNormally);
        model.addAttribute("isPointUsedNormally", isPointUsedNormally);
        model.addAttribute("isPointAccumulatedNormally", isPointAccumulatedNormally);
        model.addAttribute("isProductInventoryReducedNormally", isProductInventoryReducedNormally);
        model.addAttribute("isOrderStatusChangedToCompletedPayment",
            isOrderStatusChangedToCompletedPayment);
        model.addAttribute("tossPaymentsResponseDto", tossPaymentsResponseDto);
        return "view/payment/success";
    }

    @GetMapping("/client/order/{orderId}/payment/fail")
    public String paymentResult(
        @PathVariable long orderId, Model model, @RequestParam(value = "message") String message,
        @RequestParam(value = "code") Integer code) {
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        return "view/payment/failed";
    }
}