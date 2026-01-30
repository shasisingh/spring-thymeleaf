package com.nightcrawler.spring.hostel.service;

import com.nightcrawler.spring.hostel.model.Allocation;
import com.nightcrawler.spring.hostel.repository.AllocationRepository;
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
    public List<Allocation> findByHostelId(Long hostelId) { return repo.findByHostelId(hostelId); }
}
