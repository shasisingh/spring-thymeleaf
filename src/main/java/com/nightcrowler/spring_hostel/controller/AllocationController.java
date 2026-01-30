package com.nightcrowler.spring_hostel.controller;

import com.nightcrowler.spring_hostel.model.Allocation;
import com.nightcrowler.spring_hostel.service.AllocationService;
import com.nightcrowler.spring_hostel.service.HostelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/allocations")
public class AllocationController {

    private final AllocationService allocationService;
    private final HostelService hostelService;

    public AllocationController(AllocationService allocationService, HostelService hostelService) {
        this.allocationService = allocationService;
        this.hostelService = hostelService;
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("allocation", new Allocation());
        model.addAttribute("identityDocs", List.of("Passport", "ID Card", "Driver License"));
        model.addAttribute("paymentMethods", List.of("Cash", "Card", "Online"));
        model.addAttribute("hostels", hostelService.listAvailable());
        return "allocations/create";
    }

    @PostMapping("/create")
    public String createSubmit(@ModelAttribute("allocation") Allocation allocation,
                               BindingResult result,
                               Model model,
                               @RequestParam(name = "hostelSelect", required = false) Long hostelId,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("identityDocs", List.of("Passport", "ID Card", "Driver License"));
            model.addAttribute("paymentMethods", List.of("Cash", "Card", "Online"));
            model.addAttribute("hostels", hostelService.listAvailable());
            return "allocations/create";
        }
        LocalDateTime in = allocation.getCheckIn();
        LocalDateTime out = allocation.getCheckOut();
        if (in != null && out != null && (out.isBefore(in) || out.isEqual(in))) {
            result.rejectValue("checkOut", "invalid", "Check-out must be after check-in");
            model.addAttribute("identityDocs", List.of("Passport", "ID Card", "Driver License"));
            model.addAttribute("paymentMethods", List.of("Cash", "Card", "Online"));
            model.addAttribute("hostels", hostelService.listAvailable());
            return "allocations/create";
        }
        if (hostelId != null) {
            hostelService.findById(hostelId)
                    .ifPresent(hostel -> allocation.setHostelName(hostel.getName()));
        }
        allocationService.save(allocation);
        redirectAttributes.addFlashAttribute("success", "Allocation created successfully");
        return "redirect:/allocations";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("allocations", allocationService.findAll());
        return "allocations/list";
    }
}
