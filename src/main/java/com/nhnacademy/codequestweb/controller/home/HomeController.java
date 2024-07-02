package com.nhnacademy.codequestweb.controller.home;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String index(HttpServletRequest req) {
        if (req.getParameter("alterMessage") != null) {
            req.setAttribute("alterMessage", req.getParameter("alterMessage"));
        }
        return "index";
    }
}
