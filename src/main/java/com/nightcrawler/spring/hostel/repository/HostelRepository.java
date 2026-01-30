package com.nightcrawler.spring.hostel.repository;

import com.nightcrawler.spring.hostel.model.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HostelRepository extends JpaRepository<Hostel, Long> {
    List<Hostel> findByAvailableTrue();
}
