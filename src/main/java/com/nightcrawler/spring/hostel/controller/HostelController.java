package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.model.ReviewForm;
import com.nightcrawler.spring.hostel.service.AllocationService;
import com.nightcrawler.spring.hostel.service.HostelService;
import com.nightcrawler.spring.hostel.service.ReviewService;
import com.nightcrawler.spring.hostel.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/hostels")
@RequiredArgsConstructor
public class HostelController {

    private final HostelService service;
    private final AllocationService allocationService;
    private final RoomService roomService;
    private final ReviewService reviewService;

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
        populateHostelDetail(id, page, model);
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
            populateHostelDetail(id, page, model);
            return "hostels/detail";
        }
        reviewService.createForHostel(form, hostelOpt.get());
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
        } catch (Exception ignored) {
        }
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

    // Helper to populate detail view model for a hostel, avoiding code repetition
    private void populateHostelDetail(Long id, int page, Model model) {
        var hostel = service.findById(id).orElse(null);
        model.addAttribute("hostel", hostel);
        if (hostel == null) {
            return; // template handles null hostel case
        }
        var allocations = allocationService.findByHostelId(id);
        model.addAttribute("allocations", allocations);
        int allocationCount = allocations != null ? allocations.size() : 0;
        boolean hasAllocations = allocationCount > 0;
        long roomCount = 0;
        try {
            roomCount = roomService.countByHostel(id);
        } catch (Exception ignored) {
        }
        boolean hasRooms = roomCount > 0;
        model.addAttribute("hasRooms", hasRooms);
        model.addAttribute("hasAllocations", hasAllocations);
        model.addAttribute("roomCount", roomCount);
        model.addAttribute("allocationCount", allocationCount);
        long reviewCount = reviewService.countByHostelId(id);
        model.addAttribute("reviewCount", reviewCount);
        int pageSize = 5;
        var reviewPage = reviewService.pageByHostel(id, page, pageSize);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("reviews", reviewPage.getContent());
        if (!model.containsAttribute("reviewForm")) {
            model.addAttribute("reviewForm", new ReviewForm());
        }
    }
}
