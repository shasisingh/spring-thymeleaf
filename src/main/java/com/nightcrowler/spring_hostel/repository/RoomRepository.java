package com.nightcrowler.spring_hostel.repository;

import com.nightcrowler.spring_hostel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostelIdAndAllocatedFalse(Long hostelId);
    List<Room> findByHostelId(Long hostelId);
}
