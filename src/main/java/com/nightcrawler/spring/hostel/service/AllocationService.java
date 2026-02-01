package com.nightcrawler.spring.hostel.service;

import com.nightcrawler.spring.hostel.model.Allocation;
import com.nightcrawler.spring.hostel.repository.AllocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllocationService {
    private final AllocationRepository repo;
    private final HostelService hostelService;
    private final RoomService roomService;

    public void save(Allocation a) {
        repo.save(a);
    }

    public void createDefaultForm(Long hostelId, Model model) {
        var allocation = new Allocation();
        // Set sensible defaults for datetime fields
        var now = LocalDateTime.now().withSecond(0).withNano(0);
        allocation.setCheckIn(now);
        allocation.setDob(now.minusYears(18).toLocalDate());
        allocation.setCheckOut(now.plusDays(2));
        allocation.setHostelId(hostelId);
        model.addAttribute("allocation", allocation);
        model.addAttribute("identityDocs", List.of("Passport", "ID Card", "Driver License"));
        model.addAttribute("paymentMethods", List.of("Cash", "Card", "Online"));
        model.addAttribute("hostels", hostelService.listAvailable());
        model.addAttribute("rooms", roomService.availableByHostel(hostelId));
        model.addAttribute("selectedHostelId", hostelId);
    }

    public Optional<Allocation> findById(Long id) {
        return repo.findById(id);
    }

    public List<Allocation> findAll() {
        return repo.findAll();
    }

    public List<Allocation> findByHostelId(Long hostelId) {
        return repo.findByHostelIdWithRoom(hostelId);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // New: find allocations by guest email
    public List<Allocation> findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
