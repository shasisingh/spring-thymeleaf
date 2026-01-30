package com.nightcrowler.spring_hostel.service;

import com.nightcrowler.spring_hostel.model.Allocation;
import com.nightcrowler.spring_hostel.repository.AllocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllocationService {
    private final AllocationRepository repo;

    public AllocationService(AllocationRepository repo) {
        this.repo = repo;
    }

    public Allocation save(Allocation a) { return repo.save(a); }
    public List<Allocation> findAll() { return repo.findAll(); }
}
