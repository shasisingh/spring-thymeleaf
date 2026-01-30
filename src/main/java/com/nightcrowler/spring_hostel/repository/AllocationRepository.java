package com.nightcrowler.spring_hostel.repository;

import com.nightcrowler.spring_hostel.model.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
}
