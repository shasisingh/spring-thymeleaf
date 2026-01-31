package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.service.AllocationService;
import com.nightcrawler.spring.hostel.service.HostelService;
import com.nightcrawler.spring.hostel.service.RoomService;
import com.nightcrawler.spring.hostel.repository.ReviewRepository;
import com.nightcrawler.spring.hostel.model.ReviewForm;
import com.nightcrawler.spring.hostel.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/hostels")
@RequiredArgsConstructor
public class HostelController {

    private final HostelService service;
    private final AllocationService allocationService;
    private final RoomService roomService;
    private final ReviewRepository reviewRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("hostels", service.listAll());
        return "hostels/list";
    }

    @GetMapping("/available")
    public String available(Model model) {
        model.addAttribute("hostels", service.listAvailable());
        return "hostels/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("hostel", new Hostel());
        return "hostels/create";
    }

    @PostMapping("/create")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createSubmit(@ModelAttribute Hostel hostel, Errors errors, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        if (errors.hasErrors()) {
            return "hostels/create";
        }
        service.save(hostel);
        ra.addFlashAttribute("success", "Hostel created successfully");
        return "redirect:/api/v1/hostels";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        var hostel = service.findById(id).orElse(null);
        model.addAttribute("hostel", hostel);
        var allocations = allocationService.findByHostelId(id);
        model.addAttribute("allocations", allocations);
        int allocationCount = allocations != null ? allocations.size() : 0;
        boolean hasAllocations = allocationCount > 0;
        long roomCount = 0;
        try {
            roomCount = roomService.countByHostel(id);
        } catch (Exception ignored) {}
        boolean hasRooms = roomCount > 0;
        model.addAttribute("hasRooms", hasRooms);
        model.addAttribute("hasAllocations", hasAllocations);
        model.addAttribute("roomCount", roomCount);
        model.addAttribute("allocationCount", allocationCount);
        // reviews for this hostel with pagination
        long reviewCount = reviewRepository.countByHostel_Id(id);
        model.addAttribute("reviewCount", reviewCount);
        int pageSize = 5;
        Pageable pageable = PageRequest.of(Math.max(0, page), pageSize, Sort.by(Sort.Direction.DESC, "created"));
        Page<com.nightcrawler.spring.hostel.model.Review> reviewPage = reviewRepository.findByHostel_Id(id, pageable);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("reviewForm", new ReviewForm());
        return "hostels/detail";
    }

    @PostMapping("/{id}/reviews")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String addReview(@PathVariable Long id,
                            @Valid @ModelAttribute("reviewForm") ReviewForm form,
                            BindingResult result,
                            Model model,
                            org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                            @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        var hostelOpt = service.findById(id);
        if (hostelOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Hostel not found");
            return "redirect:/api/v1/hostels";
        }
        if (result.hasErrors()) {
            // repopulate detail page
            var hostel = hostelOpt.get();
            model.addAttribute("hostel", hostel);
            var allocations = allocationService.findByHostelId(id);
            model.addAttribute("allocations", allocations);
            int allocationCount = allocations != null ? allocations.size() : 0;
            boolean hasAllocations = allocationCount > 0;
            long roomCount = 0;
            try { roomCount = roomService.countByHostel(id); } catch (Exception ignored) {}
            boolean hasRooms = roomCount > 0;
            model.addAttribute("hasRooms", hasRooms);
            model.addAttribute("hasAllocations", hasAllocations);
            model.addAttribute("roomCount", roomCount);
            model.addAttribute("allocationCount", allocationCount);
            long reviewCount = reviewRepository.countByHostel_Id(id);
            model.addAttribute("reviewCount", reviewCount);
            int pageSize = 5;
            Pageable pageable = PageRequest.of(Math.max(0, page), pageSize, Sort.by(Sort.Direction.DESC, "created"));
            Page<com.nightcrawler.spring.hostel.model.Review> reviewPage = reviewRepository.findByHostel_Id(id, pageable);
            model.addAttribute("reviewPage", reviewPage);
            model.addAttribute("reviews", reviewPage.getContent());
            return "hostels/detail";
        }
        var hostel = hostelOpt.get();
        com.nightcrawler.spring.hostel.model.Review review = new com.nightcrawler.spring.hostel.model.Review(form.getText(), form.getAuthor());
        review.setHostel(hostel);
        reviewRepository.save(review);
        ra.addFlashAttribute("success", "Thanks for your review!");
        return "redirect:/api/v1/hostels/" + id;
    }

    @GetMapping("/{id}/rooms-page")
    public String roomsForHostel(@PathVariable Long id, Model model) {
        var hostel = service.findById(id).orElse(null);
        model.addAttribute("hostel", hostel);
        model.addAttribute("rooms", roomService.byHostel(id));
        return "hostels/rooms";
    }

    @PostMapping("/{id}/delete")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String delete(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        // Block delete if rooms exist or allocations present
        boolean hasRooms = false;
        try {
            hasRooms = roomService.countByHostel(id) > 0;
        } catch (Exception ignored) {}
        boolean hasAllocations = false;
        try {
            var allocs = allocationService.findByHostelId(id);
            hasAllocations = allocs != null && !allocs.isEmpty();
        } catch (Exception ignored) {
        }
        if (hasRooms || hasAllocations) {
            ra.addFlashAttribute("error", "Cannot delete hostel: rooms or bookings exist. Delete rooms/bookings first.");
            return "redirect:/api/v1/hostels/" + id;
        }
        service.delete(id);
        ra.addFlashAttribute("success", "Hostel deleted successfully");
        return "redirect:/api/v1/hostels";
    }
}
