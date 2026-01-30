package com.nightcrowler.spring_hostel.controller;

import com.nightcrowler.spring_hostel.model.Hostel;
import com.nightcrowler.spring_hostel.service.HostelService;
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

    public HostelController(HostelService service) {
        this.service = service;
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
        var h = service.findById(id).orElse(null);
        model.addAttribute("hostel", h);
        return "hostels/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/hostels";
    }
}
