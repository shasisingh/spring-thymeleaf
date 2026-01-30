package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.model.Room;
import com.nightcrawler.spring.hostel.model.RoomForm;
import com.nightcrawler.spring.hostel.repository.HostelRepository;
import com.nightcrawler.spring.hostel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomFormController {
    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;

    @GetMapping("/rooms/create")
    public String showCreateRoomForm(Model model) {
        List<Hostel> hostels = hostelRepository.findAll();
        model.addAttribute("hostels", hostels);
        model.addAttribute("roomForm", new RoomForm());
        return "rooms/create";
    }

    @PostMapping("/rooms/create")
    public String createRoom(@ModelAttribute RoomForm roomForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("hostels", hostelRepository.findAll());
            return "rooms/create";
        }
        Hostel hostel = hostelRepository.findById(roomForm.getHostelId()).orElse(null);
        if (hostel == null) {
            result.rejectValue("hostelId", "error.roomForm", "Hostel not found");
            model.addAttribute("hostels", hostelRepository.findAll());
            return "rooms/create";
        }
        Room room = new Room();
        room.setRoomNumber(roomForm.getRoomNumber());
        room.setHostel(hostel);
        room.setAllocated(false);
        roomRepository.save(room);
        return "redirect:/hostels/" + hostel.getId();
    }
}
