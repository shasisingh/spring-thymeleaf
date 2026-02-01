package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Allocation;
import com.nightcrawler.spring.hostel.model.GuestSession;
import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.model.ReviewForm;
import com.nightcrawler.spring.hostel.service.AllocationService;
import com.nightcrawler.spring.hostel.service.HostelService;
import com.nightcrawler.spring.hostel.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/guest")
@SessionAttributes("guestSession")
@RequiredArgsConstructor
public class GuestController {

    private final AllocationService allocationService;
    private final ReviewService reviewService;
    private final HostelService hostelService;
    private final Map<String, Long> lastLoginAttempt = new ConcurrentHashMap<>();
    private static final long LOGIN_COOLDOWN_MS = 30_000L;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @ModelAttribute("guestSession")
    public GuestSession guestSession() {
        return new GuestSession();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "guest/login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("guestSession") GuestSession guestSession,
                          String email,
                          RedirectAttributes ra) {
        if (email == null || email.isBlank()) {
            ra.addFlashAttribute("error", "Please enter a valid email.");
            return "redirect:/api/v1/guest/login";
        }
        email = email.trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            ra.addFlashAttribute("error", "Invalid email format.");
            return "redirect:/api/v1/guest/login";
        }
        long now = System.currentTimeMillis();
        Long last = lastLoginAttempt.get(email);
        if (last != null && now - last < LOGIN_COOLDOWN_MS) {
            ra.addFlashAttribute("error", "Please wait a moment before trying again.");
            return "redirect:/api/v1/guest/login";
        }
        lastLoginAttempt.put(email, now);
        // Verify there is at least one allocation with this email
        var allocations = allocationService.findByEmail(email);
        if (allocations.isEmpty()) {
            ra.addFlashAttribute("error", "We couldn't find any bookings with that email.");
            return "redirect:/api/v1/guest/login";
        }
        guestSession.setEmail(email);
        // Set display name from first allocation fullName if available
        var displayName = allocations.stream()
                .map(Allocation::getFullName)
                .filter(n -> n != null && !n.isBlank())
                .findFirst()
                .orElse(null);
        guestSession.setName(displayName);
        ra.addFlashAttribute("success", "Logged in successfully.");
        return "redirect:/api/v1/guest/my-rooms";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status, RedirectAttributes ra) {
        status.setComplete();
        ra.addFlashAttribute("success", "You have been logged out.");
        return "redirect:/api/v1/guest/login";
    }

    @GetMapping("/my-rooms")
    public String myRooms(@ModelAttribute("guestSession") GuestSession guestSession, Model model) {
        if (!guestSession.isLoggedIn()) {
            return "redirect:/api/v1/guest/login";
        }
        model.addAttribute("allocations", allocationService.findByEmail(guestSession.getEmail()));
        return "guest/my-rooms";
    }

    @GetMapping("/my-reviews")
    public String myReviews(@ModelAttribute("guestSession") GuestSession guestSession, Model model) {
        if (!guestSession.isLoggedIn()) {
            return "redirect:/api/v1/guest/login";
        }
        // Fetch reviews by both guest name and email to cover all cases
        var authors = new java.util.ArrayList<String>();
        if (guestSession.getEmail() != null) authors.add(guestSession.getEmail());
        if (guestSession.getName() != null && !guestSession.getName().isBlank()) authors.add(guestSession.getName());
        model.addAttribute("reviews", reviewService.findByAuthors(authors));
        model.addAttribute("reviewPage", null);
        return "guest/my-reviews";
    }

    @GetMapping("/reviews/create")
    public String createReviewForm(@ModelAttribute("guestSession") GuestSession guestSession, Model model) {
        if (!guestSession.isLoggedIn()) {
            return "redirect:/api/v1/guest/login";
        }
        model.addAttribute("reviewForm", new ReviewForm());
        // Provide dropdown of hostels from the guest's allocations
        var hostelIds = allocationService.findByEmail(guestSession.getEmail())
                .stream()
                .map(Allocation::getHostelId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        var hostels = hostelIds.stream()
                .map(id -> hostelService.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        model.addAttribute("hostels", hostels);
        return "guest/review-create";
    }

    @PostMapping("/reviews/create")
    public String createReview(@ModelAttribute("guestSession") GuestSession guestSession,
                               @Valid @ModelAttribute("reviewForm") ReviewForm form,
                               BindingResult result,
                               Long hostelId,
                               RedirectAttributes ra) {
        if (!guestSession.isLoggedIn()) {
            return "redirect:/api/v1/guest/login";
        }
        if (result.hasErrors()) {
            return "guest/review-create";
        }
        // Set author from name if available, otherwise email
        var author = (guestSession.getName() != null && !guestSession.getName().isBlank())
                ? guestSession.getName() : guestSession.getEmail();
        form.setAuthor(author);
        // If hostelId provided and belongs to guest allocations, tie review to hostel
        Hostel hostel = null;
        if (hostelId != null) {
            var allowed = allocationService
                    .findByEmail(guestSession.getEmail())
                    .stream().anyMatch(a -> hostelId.equals(a.getHostelId()));
            if (allowed) {
                hostel = hostelService.findById(hostelId).orElse(null);
            }
        }
        if (hostel != null) {
            reviewService.createForHostel(form, hostel);
        } else {
            reviewService.createWithoutHostel(form);
        }
        ra.addFlashAttribute("success", "Thanks for your review!");
        return "redirect:/api/v1/guest/my-reviews";
    }
}
