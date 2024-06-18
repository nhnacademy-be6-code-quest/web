package com.nhnacademy.codequestweb.controller.review;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Home2Controller {

    @GetMapping("/index2")
    public String home() {
        return "/view/review/index2";
    }

}
