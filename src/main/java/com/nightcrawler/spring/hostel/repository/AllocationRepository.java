package com.nightcrawler.spring.hostel.repository;

import com.nightcrawler.spring.hostel.model.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    List<Allocation> findByHostelId(Long hostelId);

    @Query("SELECT a FROM Allocation a LEFT JOIN FETCH a.room WHERE a.hostelId = :hostelId")
    List<Allocation> findByHostelIdWithRoom(@Param("hostelId") Long hostelId);

    // Find allocations by guest email
    List<Allocation> findByEmail(String email);
}
