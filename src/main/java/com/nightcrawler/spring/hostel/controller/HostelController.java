package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.service.AllocationService;
import com.nightcrawler.spring.hostel.service.HostelService;
import com.nightcrawler.spring.hostel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/hostels")
@RequiredArgsConstructor
public class HostelController {

    private final HostelService service;
    private final AllocationService allocationService;
    private final RoomService roomService;

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
    public String detail(@PathVariable Long id, Model model) {
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
        return "hostels/detail";
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
