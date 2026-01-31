package com.nightcrawler.spring.hostel.controller;

import com.nightcrawler.spring.hostel.model.Room;
import com.nightcrawler.spring.hostel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hostels")
@RequiredArgsConstructor
public class RoomRestController {
    private final RoomService roomService;

    @GetMapping("/{hostelId}/rooms")
    public List<Room> getAvailableRooms(@PathVariable Long hostelId) {
        return roomService.availableByHostel(hostelId);
    }
}
