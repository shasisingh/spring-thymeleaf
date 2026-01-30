package com.nightcrawler.spring.hostel.repository;

import com.nightcrawler.spring.hostel.model.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    List<Allocation> findByHostelId(Long hostelId);
}
