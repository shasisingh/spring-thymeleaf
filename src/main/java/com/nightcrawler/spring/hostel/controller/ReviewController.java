package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.ReviewForm;
import com.nightcrawler.spring.hostel.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        model.addAttribute("reviewPage", null);
        return "reviews/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("reviewForm", new ReviewForm());
        return "reviews/create";
    }

    @PostMapping("/create")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String create(@Valid @ModelAttribute("reviewForm") ReviewForm form,
                         BindingResult result,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "reviews/create";
        }
        reviewService.createWithoutHostel(form);
        ra.addFlashAttribute("success", "Thanks for your review!");
        return "redirect:/api/v1/reviews";
    }
}
