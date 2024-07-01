package com.nhnacademy.codequestweb.controller.mypage;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterPhoneNumberRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientUpdatePrivacyRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.review.NoPhotoReviewResponseDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.mypage.MyPageService;
import com.nhnacademy.codequestweb.service.review.NoPhotoReviewService;
import com.nhnacademy.codequestweb.service.review.PhotoReviewService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {

    private static final int DEFAULT_PAGE_SIZE = 3;
    private final MyPageService myPageService;
    private final NoPhotoReviewService noPhotoReviewService;
    private final PhotoReviewService photoReviewService;
    private final CouponService couponService;
    @GetMapping("/mypage")
    public String mypageMain(HttpServletRequest req, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<ClientPrivacyResponseDto> response = myPageService.getPrivacy(headers);

        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "profile");
        req.setAttribute("profile", response.getBody());
        return "index";
    }

    @GetMapping("/mypage/delivary")
    public String mypageDelivery(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, "refresh") == null) {
            return "redirect:/auth";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<List<ClientDeliveryAddressResponseDto>> response = myPageService.getDeliveryAddresses(
            headers);
        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "delivaryaddress");
        req.setAttribute("clientDeliveryAddresses", response.getBody());
        return "index";
    }

    @PostMapping("/mypage/delivary")
    public String registerDeliveryAddress(
        @ModelAttribute ClientRegisterAddressRequestDto clientRegisterAddressRequestDto,
        HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<String> response = myPageService.registerAddress(headers,
            clientRegisterAddressRequestDto);
        log.info("/mypage/delivary post response: {}", response.getBody());

        return "redirect:/mypage/delivary";
    }

    @DeleteMapping("/mypage/delivary/{deliveryAddressId}")
    public ResponseEntity<String> deleteDeliveryAddress(@PathVariable Long deliveryAddressId,
        HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<String> response = myPageService.deleteDeliveryAddress(headers,
            deliveryAddressId);
        log.info("/mypage/delivary post response: {}", response.getBody());

        return ResponseEntity.ok("Successfully deleted delivery address");
    }

    @GetMapping("/mypage/withdrawal")
    public String withdrawal(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, "refresh") == null) {
            return "redirect:/auth";
        }
        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "withdrawal");
        return "index";
    }

    @DeleteMapping("/mypage/withdrawal")
    public ResponseEntity<String> withdrawal(@RequestParam("pw") String pw,
        HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
        headers.set("password", pw);

        ResponseEntity<String> response = myPageService.deleteClient(headers);
        log.info("/mypage/withdrawal post response: {}", response.getBody());

        return ResponseEntity.ok("Successfully deleted delivery address");
    }

    @GetMapping("/mypage/phone")
    public String phone(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, "refresh") == null) {
            return "redirect:/auth";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<List<ClientPhoneNumberResponseDto>> response = myPageService.getPhoneNumbers(headers);
        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "phoneNumber");
        req.setAttribute("clientPhoneNumber", response.getBody());
        return "index";
    }

    @PostMapping("/mypage/phone")
    public String addPhone(HttpServletRequest req, @ModelAttribute ClientRegisterPhoneNumberRequestDto clientRegisterPhoneNumberRequestDto) {
        if (CookieUtils.getCookieValue(req, "refresh") == null) {
            return "redirect:/auth";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<String> response = myPageService.registerPhoneNumbers(headers, clientRegisterPhoneNumberRequestDto);
        log.info("response: {}", response.getBody());
        return "redirect:/mypage/phone";
    }

    @DeleteMapping("/mypage/phone/{phoneId}")
    public ResponseEntity<String> deletePhone(@PathVariable Long phoneId, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<String> response = myPageService.deletePhoneNumber(headers, phoneId);
        log.info("/mypage/phone post response: {}", response.getBody());
        return ResponseEntity.ok("Successfully deleted phone number");
    }

    @PutMapping("/mypage")
    public String updateProfile(@ModelAttribute ClientUpdatePrivacyRequestDto clientUpdatePrivacyRequestDto, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<String> response = myPageService.updateClient(headers, clientUpdatePrivacyRequestDto);
        log.info("response: {}", response.getBody());
        return "redirect:/mypage";
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public String notFound(HttpServletRequest req, Exception e) {
        return "redirect:/auth";
    }

    @GetMapping("/mypage/reviews/no-photo")
    public String getNoPhotoReviews(HttpServletRequest req, Model model, Pageable pageable) {
        if (CookieUtils.getCookieValue(req, "refresh") == null) {
            return "redirect:/auth";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "noPhotoReviews");

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));

        ResponseEntity<Page<NoPhotoReviewResponseDTO>> noPhotoResponseEntity = noPhotoReviewService.getAllReviewsByClientId(
            headers, pageRequest);
        log.info("/mypage/reviews/no-photo response: {}", noPhotoResponseEntity.getBody());

        Page<NoPhotoReviewResponseDTO> noPhotoReviews = noPhotoResponseEntity.getBody();
        model.addAttribute("reviews", noPhotoReviews);

        return "index";
    }

    @GetMapping("/mypage/reviews/photo")
    public String getPhotoReviews(HttpServletRequest req, Model model, Pageable pageable) {
        if (CookieUtils.getCookieValue(req, "refresh") == null) {
            return "redirect:/auth";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "photoReviews");

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));

        ResponseEntity<Page<PhotoReviewResponseDTO>> photoResponseEntity = photoReviewService.getAllReviewsByClientId(
            headers, pageRequest);
        log.info("/mypage/reviews/photo response: {}", photoResponseEntity.getBody());

        Page<PhotoReviewResponseDTO> photoReviews = photoResponseEntity.getBody();
        model.addAttribute("reviews", photoReviews);

        return "index";
    }


    @GetMapping("/mypage/coupon")
    public String getCoupon(HttpServletRequest req, Model model, Pageable pageable) {
//        if (CookieUtils.getCookieValue(req, "refresh") == null) {
//            return "redirect:/auth";
//        }

//
        HttpHeaders headers = new HttpHeaders();
//        headers.set("access", CookieUtils.getCookieValue(req, "access"));
//        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
headers.set("X-User-id","1");
        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "coupon");

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
                Sort.Direction.DESC, "registerDate"));

        List<CouponResponseDto> couponList = couponService.findClientCoupon(headers);
        //TODO pageable 고려
        model.addAttribute("coupons", couponList);
        return "index";
    }

    @GetMapping("/api/client/coupon")
    public String viewCoupon(Model model,HttpServletRequest req){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
        List<CouponResponseDto> couponList = couponService.findClientCoupon(headers);
        model.addAttribute("couponList",couponList);
        return "/view/coupon/client_coupon_view";
    }


}
