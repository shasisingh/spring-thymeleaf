package com.nightcrawler.spring.hostel.repository;

import com.nightcrawler.spring.hostel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostelIdAndAllocatedFalse(Long hostelId);

    List<Room> findByHostelId(Long hostelId);

    long countByHostel_Id(Long hostelId);
}
