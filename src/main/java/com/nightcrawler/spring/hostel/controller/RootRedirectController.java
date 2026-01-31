package com.nightcrawler.spring.hostel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    @GetMapping("/")
    public String redirectToApiV1() {
        return "redirect:/api/v1";
    }

    @GetMapping("/index")
    public String redirectIndexToApiV1Index() {
        return "redirect:/api/v1/index";
    }
}
