package com.nightcrawler.spring.hostel.repository;

import com.nightcrawler.spring.hostel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostelIdAndAllocatedFalse(Long hostelId);

    List<Room> findByHostelId(Long hostelId);
}
