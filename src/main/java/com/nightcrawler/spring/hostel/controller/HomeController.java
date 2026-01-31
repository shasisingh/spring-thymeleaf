package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HomeController {

    private final ReviewRepository reviewRepository;

    @GetMapping({"", "/", "/index"})
    public String home(Model model) {
        model.addAttribute("reviews", reviewRepository.findTop5ByOrderByCreatedDesc(PageRequest.of(0, 5)));
        return "index";
    }
}
