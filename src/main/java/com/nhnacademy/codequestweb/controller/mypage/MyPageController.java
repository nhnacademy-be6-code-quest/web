package com.nhnacademy.codequestweb.controller.mypage;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final UserClient userClient;

    @GetMapping("/mypage")
    public String mypageMain(HttpServletRequest req, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
//        userClient.test(headers);
        req.setAttribute("view", "mypage");
        return "index";
    }
}
