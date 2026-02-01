package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Allocation;
import com.nightcrawler.spring.hostel.service.AllocationService;
import com.nightcrawler.spring.hostel.service.HostelService;
import com.nightcrawler.spring.hostel.service.RoomService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/v1/allocations")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;
    private final HostelService hostelService;
    private final RoomService roomService;

    @GetMapping("/create")
    public String createForm(@RequestParam(value = "hostelId", required = false) Long hostelId,
                             Model model) {
        allocationService.createDefaultForm(hostelId, model);
        return "allocations/create";
    }


    @PostMapping("/create")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createSubmit(@ModelAttribute("allocation") Allocation allocation,
                               BindingResult result,
                               Model model,
                               @RequestParam(name = "hostelId") @NotNull Long hostelId,
                               @RequestParam(value = "roomId") @NotNull Long roomId,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return returnError(model, hostelId, roomId);
        }
        
        var in = allocation.getCheckIn();
        var out = allocation.getCheckOut();
        if (in != null && out != null && (out.isBefore(in) || out.isEqual(in))) {
            result.rejectValue("checkOut", "invalid", "Check-out must be after check-in");
            model.addAttribute("identityDocs", List.of("Passport", "ID Card", "Driver License"));
            model.addAttribute("paymentMethods", List.of("Cash", "Card", "Online"));
            model.addAttribute("hostels", hostelService.listAvailable());
            return "allocations/create";
        }

        hostelService.findById(hostelId)
                .ifPresent(hostel -> {
                    allocation.setHostelName(hostel.getName());
                    allocation.setHostelId(hostel.getId());
                    var room = roomService.findById(roomId);
                    if (room != null && room.isAllocated()) {
                        result.rejectValue("hostelRoomNumber", "allocated", "Selected room is already allocated");
                    }
                    allocation.setRoom(room);
                    if (room != null && !result.hasErrors()) {
                        room.setAllocated(true);
                        roomService.save(room);
                        allocation.setHostelRoomNumber(room.getRoomNumber());
                    }
                });

        allocationService.save(allocation);
        redirectAttributes.addFlashAttribute("success", "Allocation created successfully");
        return "redirect:/api/v1/allocations";
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("allocations", allocationService.findAll());
        return "allocations/list";
    }

    @PostMapping("/{id}/delete")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String delete(@PathVariable Long id,
                         @RequestParam(name = "hostelId") Long hostelId,
                         RedirectAttributes ra) {
        try {
            allocationService.findById(id)
                    .ifPresent(a -> {
                        if (a.getRoom() != null) {
                            var r = a.getRoom();
                            r.setAllocated(false);
                            roomService.save(r);
                        }
                    });
            allocationService.deleteById(id);
            ra.addFlashAttribute("success", "Allocation deleted successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Allocation not found or already deleted");
        }
        if (hostelId != null) {
            return "redirect:/api/v1/hostels/" + hostelId;
        }
        return "redirect:/api/v1/allocations";
    }

    private @NonNull String returnError(Model model, Long hostelId, Long roomId) {
        model.addAttribute("identityDocs", List.of("Passport", "ID Card", "Driver License"));
        model.addAttribute("paymentMethods", List.of("Cash", "Card", "Online"));
        model.addAttribute("hostels", hostelService.listAvailable());
        model.addAttribute("rooms", roomService.availableByHostel(hostelId));
        model.addAttribute("selectedHostelId", hostelId);
        model.addAttribute("selectedRoomId", roomId);

        return "allocations/create";
    }

}
