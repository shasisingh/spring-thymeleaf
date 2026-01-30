package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.service.AllocationService;
import com.nightcrawler.spring.hostel.service.HostelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hostels")
public class HostelController {

    private final HostelService service;
    private final AllocationService allocationService;

    public HostelController(HostelService service, AllocationService allocationService) {
        this.service = service;
        this.allocationService = allocationService;
    }

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
    public String createSubmit(@ModelAttribute Hostel hostel, Errors errors) {
        if (errors.hasErrors()) {
            return "hostels/create";
        }
        service.save(hostel);
        return "redirect:/hostels";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Hostel hostel = service.findById(id).orElse(null);
        model.addAttribute("hostel", hostel);
        model.addAttribute("allocations", allocationService.findByHostelId(id));
        return "hostels/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/hostels";
    }
}
