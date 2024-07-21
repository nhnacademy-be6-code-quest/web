package com.nhnacademy.codequestweb.controller.home;

import com.nhnacademy.codequestweb.service.home.HomeService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private static final String ALTER_MESSAGE = "alterMessage";
    private final HomeService homeService;

    @GetMapping("/")
    public String index(HttpServletRequest req) {
        if (req.getParameter(ALTER_MESSAGE) != null) {
            req.setAttribute(ALTER_MESSAGE, req.getParameter(ALTER_MESSAGE));
        }
        req.setAttribute("popularBooks", homeService.getPopularBooks(CookieUtils.setHeader(req)));
        req.setAttribute("newBooks", homeService.getNewBooks(CookieUtils.setHeader(req)));
        req.setAttribute("almostSoldOutBooks", homeService.getAlmostSoldOutBooks(CookieUtils.setHeader(req)));
        return "index";
    }
}
