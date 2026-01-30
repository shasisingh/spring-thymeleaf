package com.nightcrowler.spring_hostel.repository;

import com.nightcrowler.spring_hostel.model.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HostelRepository extends JpaRepository<Hostel, Long> {
    List<Hostel> findByAvailableTrue();
}
