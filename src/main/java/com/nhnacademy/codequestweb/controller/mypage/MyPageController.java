package com.nhnacademy.codequestweb.controller.mypage;

import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterPhoneNumberRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientUpdatePrivacyRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponMyPageCouponResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationMyPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageMyPageResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.mypage.MyPageService;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.point.PointAccumulationService;
import com.nhnacademy.codequestweb.service.point.PointUsageService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {

    private static final int DEFAULT_PAGE_SIZE = 3;
    private final MyPageService myPageService;
    private final PointUsageService pointUsageService;
    private final PointAccumulationService pointAccumulationService;
    private final CouponService couponService;
    private final OrderService orderService;

    @GetMapping("/mypage")
    public String mypageMain(HttpServletRequest req, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        ResponseEntity<ClientPrivacyResponseDto> response = myPageService.getPrivacy(headers);

        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "profile");
        req.setAttribute("activeSection", "client");
        req.setAttribute("profile", response.getBody());
        return "index";
    }

    @GetMapping("/mypage/delivary")
    public String mypageDelivery(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, "access") == null) {
            return "redirect:/auth";
        } else if (req.getParameter("alterMessage") != null) {
            String decoding;
            try{
                decoding = URLDecoder.decode(req.getParameter("alterMessage"), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                decoding = null;
            }
            req.setAttribute("alterMessage", decoding);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        ResponseEntity<List<ClientDeliveryAddressResponseDto>> response = myPageService.getDeliveryAddresses(headers);
        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "delivaryaddress");
        req.setAttribute("activeSection", "client");
        req.setAttribute("clientDeliveryAddresses", response.getBody());
        return "index";
    }

    @PostMapping("/mypage/delivary")
    public String registerDeliveryAddress(
        @ModelAttribute ClientRegisterAddressRequestDto clientRegisterAddressRequestDto,
        HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        try {
            ResponseEntity<String> response = myPageService.registerAddress(headers,
                    clientRegisterAddressRequestDto);
            log.info("/mypage/delivary post response: {}", response.getBody());
        } catch (FeignException e) {
            String message = "배송지는 최대 10개 까지 등록 할 수 있습니다.";
            String encodedMessage;
            try {
                encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e1) {
                encodedMessage = message;
            }
            return "redirect:/mypage/delivary?alterMessage=" + encodedMessage;
        }
        return "redirect:/mypage/delivary";
    }

    @DeleteMapping("/mypage/delivary/{deliveryAddressId}")
    public ResponseEntity<String> deleteDeliveryAddress(@PathVariable Long deliveryAddressId,
        HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        ResponseEntity<String> response = myPageService.deleteDeliveryAddress(headers,
            deliveryAddressId);
        log.info("/mypage/delivary post response: {}", response.getBody());

        return ResponseEntity.ok("Successfully deleted delivery address");
    }

    @GetMapping("/mypage/withdrawal")
    public String withdrawal(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, "access") == null) {
            return "redirect:/auth";
        }
        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "withdrawal");
        req.setAttribute("activeSection", "client");
        return "index";
    }

    @DeleteMapping("/mypage/withdrawal")
    public ResponseEntity<String> withdrawal(@RequestParam("pw") String pw,
        HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("password", pw);
        try {
            return myPageService.deleteClient(headers);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/mypage/phone")
    public String phone(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, "access") == null) {
            return "redirect:/auth";
        } else if (req.getParameter("alterMessage") != null) {
            req.setAttribute("alterMessage", req.getParameter("alterMessage"));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        ResponseEntity<List<ClientPhoneNumberResponseDto>> response = myPageService.getPhoneNumbers(headers);
        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "phoneNumber");
        req.setAttribute("activeSection", "client");
        req.setAttribute("clientPhoneNumber", response.getBody());
        return "index";
    }

    @PostMapping("/mypage/phone")
    public String addPhone(HttpServletRequest req, @Valid @ModelAttribute ClientRegisterPhoneNumberRequestDto clientRegisterPhoneNumberRequestDto) {
        if (CookieUtils.getCookieValue(req, "access") == null) {
            return "redirect:/auth";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        ResponseEntity<String> response = myPageService.registerPhoneNumbers(headers, clientRegisterPhoneNumberRequestDto);
        log.info("response: {}", response.getBody());
        return "redirect:/mypage/phone";
    }

    @DeleteMapping("/mypage/phone/{phoneId}")
    public ResponseEntity<String> deletePhone(@PathVariable Long phoneId, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        ResponseEntity<String> response = myPageService.deletePhoneNumber(headers, phoneId);
        log.info("/mypage/phone post response: {}", response.getBody());
        return ResponseEntity.ok("Successfully deleted phone number");
    }

    @PutMapping("/mypage")
    public String updateProfile(@ModelAttribute ClientUpdatePrivacyRequestDto clientUpdatePrivacyRequestDto, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        ResponseEntity<String> response = myPageService.updateClient(headers, clientUpdatePrivacyRequestDto);
        log.info("response: {}", response.getBody());
        return "redirect:/mypage";
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public String notFound(HttpServletRequest req, Exception e) {
        return "redirect:/auth";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String validationError(MethodArgumentNotValidException e, HttpServletRequest req) {
        String encodedMessage;
        try {
            encodedMessage = URLEncoder.encode(e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e1) {
            encodedMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        String referer = req.getHeader("Referer").split("\\?alterMessage")[0];
        return "redirect:" + (referer != null ? referer : "/") + "?alterMessage=" + encodedMessage;
    }

    @GetMapping("/mypage/point/reward")
    public String myPageRewardPoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "pointReward");
        req.setAttribute("activeSection", "coupon");
        Page<PointAccumulationMyPageResponseDto> dto = pointAccumulationService.clientPoint(headers, page, size);
        req.setAttribute("points", dto);
        return "index";
    }

    @GetMapping("/mypage/point/use")
    public String myPageUsedPoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "pointUsed");
        req.setAttribute("activeSection", "point");
        Page<PointUsageMyPageResponseDto> dto = pointUsageService.clientUsePoint(headers, page, size);
        req.setAttribute("points", dto);
        return "index";
    }

    @GetMapping("/mypage/orders")
    public String mypageOrders(HttpServletRequest req,
                               @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                               @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo){

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "orders");
        req.setAttribute("activeSection", "order");

        Page<ClientOrderGetResponseDto> orderResponseDtoList = orderService.getClientOrders(headers, pageSize, pageNo, "orderDatetime", "desc");

        req.setAttribute("orders", orderResponseDtoList.getContent());
        req.setAttribute("totalPages", orderResponseDtoList.getTotalPages());
        req.setAttribute("currentPage", orderResponseDtoList.getNumber());
        req.setAttribute("pageSize", orderResponseDtoList.getSize());

        return "index";
    }

    @GetMapping("/mypage/my-review")
    public String mypageReview(HttpServletRequest req,
                               @RequestParam(name = "page") int page) {
        Page<ReviewInfoResponseDto> reivewInfoPage = myPageService.getMyReviewInfo(CookieUtils.getCookieValue(req, "access"), page, 5);
        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "review");
        req.setAttribute("reviews", reivewInfoPage.getContent());
        req.setAttribute("totalPage", reivewInfoPage.getTotalPages());
        req.setAttribute("page", page);
        req.setAttribute("activeSection", "review");
        return "index";
    }
}
