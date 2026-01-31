package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Review;
import com.nightcrawler.spring.hostel.model.ReviewForm;
import com.nightcrawler.spring.hostel.repository.ReviewRepository;
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

    private final ReviewRepository reviewRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
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
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "reviews/create";
        }
        Review review = new Review(form.getText(), form.getAuthor());
        review.setRating(form.getRating());
        reviewRepository.save(review);
        ra.addFlashAttribute("success", "Thanks for your review!");
        return "redirect:/api/v1/reviews";
    }
}
