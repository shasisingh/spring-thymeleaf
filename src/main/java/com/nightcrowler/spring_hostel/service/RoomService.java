package com.nightcrowler.spring_hostel.service;

import com.nightcrowler.spring_hostel.model.Room;
import com.nightcrowler.spring_hostel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository repo;

    public RoomService(RoomRepository repo) { this.repo = repo; }

    public List<Room> availableByHostel(Long hostelId) { return repo.findByHostelIdAndAllocatedFalse(hostelId); }
    public List<Room> byHostel(Long hostelId) { return repo.findByHostelId(hostelId); }
    public Room save(Room r) { return repo.save(r); }
    public Room findById(Long id) { return repo.findById(id).orElse(null); }
}
