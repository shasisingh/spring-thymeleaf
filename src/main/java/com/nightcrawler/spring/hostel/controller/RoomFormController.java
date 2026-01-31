package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Hostel;
import com.nightcrawler.spring.hostel.model.Room;
import com.nightcrawler.spring.hostel.model.RoomForm;
import com.nightcrawler.spring.hostel.repository.HostelRepository;
import com.nightcrawler.spring.hostel.repository.RoomRepository;
import com.nightcrawler.spring.hostel.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static java.util.Comparator.comparing;

@Controller
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomFormController {
    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;
    private final AllocationService allocationService;

    @GetMapping("/create")
    public String showCreateRoomForm(Model model,
                                     @RequestParam(name = "hostelId", required = false) Long hostelId,
                                     @RequestParam(name = "ref", required = false) String ref) {
        List<Hostel> hostels = getAllHostelsSorted();
        model.addAttribute("hostels", hostels);
        RoomForm form = new RoomForm();
        if (hostelId != null) {
            form.setHostelId(hostelId);
        }
        model.addAttribute("roomForm", form);
        if (ref != null) {
            model.addAttribute("ref", ref);
        }
        return "rooms/create";
    }

    @PostMapping("/create")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createRoom(@ModelAttribute RoomForm roomForm, BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("hostels", getAllHostelsSorted());
            return "rooms/create";
        }
        var hostel = hostelRepository.findById(roomForm.getHostelId()).orElse(null);
        if (hostel == null) {
            result.rejectValue("hostelId", "error.roomForm", "Hostel not found");
            model.addAttribute("hostels", getAllHostelsSorted());
            return "rooms/create";
        }
        createNewRoom(roomForm, hostel);
        // increment capacity for the hostel
        hostel.setCapacity(hostel.getCapacity() + 1);
        hostelRepository.save(hostel);
        ra.addFlashAttribute("success", "Room created successfully");
        return "redirect:/api/v1/hostels/" + hostel.getId();
    }


    @PostMapping("/{id}/delete")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String deleteRoom(@PathVariable Long id,
                             @RequestParam(name = "hostelId", required = false) Long hostelId,
                             org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        var room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            ra.addFlashAttribute("error", "Room not found");
            return "redirect:/hostels";
        }
        // If room is allocated or has allocations, prevent delete
        boolean hasAllocations = allocationService.findAll()
                .stream()
                .anyMatch(a -> a.getRoom() != null && a.getRoom().getId().equals(id));
        if (room.isAllocated() || hasAllocations) {
            ra.addFlashAttribute("error", "Cannot delete: room has existing bookings");
            Long targetHostelId = hostelId != null ? hostelId : (room.getHostel() != null ? room.getHostel().getId() : null);
            return targetHostelId != null ? "redirect:/api/v1/hostels/" + targetHostelId : "redirect:/api/v1/rooms";
        }
        Long targetHostelId = hostelId != null ? hostelId : (room.getHostel() != null ? room.getHostel().getId() : null);
        roomRepository.deleteById(id);
        if (targetHostelId != null) {
            hostelRepository.findById(targetHostelId).ifPresent(h -> {
                int newCapacity = Math.max(0, h.getCapacity() - 1);
                h.setCapacity(newCapacity);
                hostelRepository.save(h);
            });
        }
        ra.addFlashAttribute("success", "Room deleted successfully");
        if (hostelId != null) {
            return "redirect:/api/v1/hostels/" + hostelId;
        }
        return "redirect:/api/v1/hostels";
    }

    @GetMapping
    public String listRooms(Model model) {
        var rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "rooms/list";
    }

    private List<Hostel> getAllHostelsSorted() {
        return hostelRepository.findAll()
                .stream()
                .sorted(comparing(Hostel::getName))
                .toList();
    }

    private void createNewRoom(RoomForm roomForm, Hostel hostel) {
        var room = new Room();
        room.setRoomNumber(roomForm.getRoomNumber());
        room.setHostel(hostel);
        room.setRoomType(roomForm.getRoomType());
        room.setAllocated(false);
        roomRepository.save(room);
    }
}
